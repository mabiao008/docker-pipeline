package com.github.jadepeng.pipeline.core.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jqpeng
 */
@Data
@Document(collection = "pipeline-job")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineJob {

    @Id
    private String id;

    @Indexed
    private String pipelineId;

    private String pipelineName;

    private Status status;

    /**
     * 执行Job的agent
     */
    private String agent;

    /**
     * 关联的任务
     */
    private List<Pipeline.PipelineTask> tasks = new ArrayList<>();

    private String currentTask;

    private int exitedValue;

    private String remark;

    private String jobLogPath;

    private List<Pipeline.Network> networks = Lists
        .newArrayList(new Pipeline.Network());

    private List<Pipeline.Volume> volumes = Lists.newArrayList(new Pipeline.Volume());

    /**
     * 工作空间地址
     */
    @Field("working_dir")
    private String workingDir;

    private Long startTime;

    private Long finishTime;
}
