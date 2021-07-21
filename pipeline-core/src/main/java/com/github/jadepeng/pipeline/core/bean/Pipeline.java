package com.github.jadepeng.pipeline.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import lombok.Builder;
import lombok.Data;

/**
 * 任务
 *
 * @author jqpeng
 */
@Data
@Document(collection = "pipeline")
public class Pipeline extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    private String name;

    @JSONField(name = "pipeline")
    private List<PipelineTask> pipelineTasks = new ArrayList<>();

    private List<Network> networks = Lists.newArrayList(new Network());

    private List<Volume> volumes = Lists.newArrayList(new Volume());

    private String startNode;

    private boolean runOnce = true;

    /**
     * 周期运行
     */
    private String crontab;

    /**
     * 是否已经逻辑删除
     */
    @Indexed
    private transient Integer isAvailable = 1;

    private transient List<String> runningPipelines = new ArrayList<>();

    private transient List<String> finishedPipeliens = new ArrayList<>();

    /**
     * 跳过节点
     */
    public void skipNodes() {
        if (this.startNode != null) {
            this.recursiveSkipNode(this.startNode);
        }
    }

    public void addPipelineTask(PipelineTask task) {
        this.pipelineTasks.add(task);
    }


    /**
     * 递归跳过节点
     *
     * @param node
     */
    private void recursiveSkipNode(String node) {
        if (node != null) {
            PipelineTask startPipeNode =
                this.pipelineTasks.stream()
                                  .filter(p -> p.getName().equals(node))
                                  .findFirst()
                                  .orElseGet(null);
            if (startPipeNode != null) {
                this.finishedPipeliens.addAll(startPipeNode.getDependencies());
                for (String subNode : startPipeNode.dependencies) {
                    // 输出日志，方便获取状态
                    System.out.println("proc \"" + subNode + "\" started");
                    System.out.println(subNode + ": 本次跳过运行");
                    System.out.println(
                        "proc \"" + subNode + "\" exited with status 0");
                    recursiveSkipNode(subNode);
                }
            }
        }
    }

    /**
     * 获取可执行的PipelineTask
     *
     * @return
     */
    @JsonIgnore
    public List<PipelineTask> getAvailablePipelineTasks() {
        List<PipelineTask> pipelines = new ArrayList<>();
        for (PipelineTask pipelineItem : this.pipelineTasks) {
            String name = pipelineItem.getName();
            if (this.runningPipelines.contains(name) ||
                this.finishedPipeliens.contains(name)) {
                continue;
            }
            if (this.isAvailable(pipelineItem)) {
                pipelines.add(pipelineItem);
            }
        }
        runningPipelines
            .addAll(pipelines.stream().map(p -> p.getName()).collect(
                Collectors.toList()));
        return pipelines;
    }

    public boolean isFinished() {
        return runningPipelines.size() == 0 &&
               finishedPipeliens.size() == pipelineTasks
                   .size();
    }

    private boolean isAvailable(PipelineTask pipeline) {
        if (pipeline.getDependencies() == null) {
            return true;
        }

        for (String pipelineName : pipeline.getDependencies()) {
            if (!finishedPipeliens.contains(pipelineName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * pipelineTask 执行完成
     *
     * @param pipeline
     */
    public void onPipelineTaskFinish(PipelineTask pipeline) {
        this.finishedPipeliens.add(pipeline.getName());
        this.runningPipelines.remove(pipeline.getName());
    }

    public static Pipeline buildDefaultJob(String name, String image) {
        Pipeline job = new Pipeline();
        job.setName(name);
        PipelineTask task = PipelineTask.builder()
                                        .name(name)
                                        .steps(Collections.singletonList(
                                            Step.builder()
                                                .name("defaultJob")
                                                .image(image)
                                                .build()
                                        )).build();
        job.addPipelineTask(task);
        return job;
    }

    @Data
    @Builder
    public static class PipelineTask {

        /**
         * 名称
         */
        String name;

        /**
         * 别名
         */
        String alias;

        /**
         * 依赖的pipelines，必须依赖的执行完成才能运行该PipelineTask
         */
        List<String> dependencies;

        /**
         * 任务步骤，顺序执行
         */
        List<Step> steps;
    }

    @Data
    public static class Network {
        String name = "pipeline_default";
        String driver = "bridge";
    }

    @Data
    public static class Volume {
        String name = "pipeline_default";
        String driver = "local";
    }

    @Data
    public static class StepNetwork {
        private String name;
        private List<String> aliases = Lists.newArrayList("default");

        public StepNetwork(String name) {
            this.name = name;
        }
    }

    @Data
    @Builder
    public static class Step {
        private String name;
        private String alias;
        private String image;
        @JSONField(name = "working_dir")
        private String workingDir = "/workspace";
        private JSONObject environment = new JSONObject();
        @JSONField(name = "entry_point")
        private List<String> entryPoint;
        private List<String> command;
        private List<String> volumes;
        private List<StepNetwork> networks =
            Lists.newArrayList(new StepNetwork("pipeline_default"));

        @JSONField(name = "on_success")
        private boolean onSuccess = true;

        @JSONField(name = "auth_config")
        private JSONObject authConfig = new JSONObject();

        public List<String> getEnvironmentString() {
            return environment.keySet().stream().map(key -> key + "=" + environment.getString(key)).collect(Collectors.toList());
        }

    }
}
