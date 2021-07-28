package com.github.jadepeng.pipeline.agent.jobrunner;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.jadepeng.pipeline.agent.config.ApplicationProperties;
import com.github.jadepeng.pipeline.agent.jobrunner.docker.ContainerVolumeBind;
import com.github.jadepeng.pipeline.agent.jobrunner.docker.DockerEngine;
import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.core.dto.ExecutePipelineRequest;
import com.github.jadepeng.pipeline.utils.UUIDUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

/**
 * @author jqpeng
 */
public class DockerRunnerEngine implements RunnerRunEngine {

    private final JobRunner jobRunner;
    private final ApplicationProperties config;
    private String extraHosts;
    private Pipeline pipeline;
    private String jobId;
    private String currentContainerId;
    private final DockerEngine dockerEngine;
    private final FilePipelingLogger fileLogger;


    public DockerRunnerEngine(JobRunner jobRunner,
                              ExecutePipelineRequest request,
                              ApplicationProperties config,
                              DockerEngine dockerEngine) throws
                                                         FileNotFoundException {
        this.jobRunner = jobRunner;
        this.config = config;
        this.jobId = request.getPipelineJobId();
        this.pipeline = request.getPipeline();
        this.dockerEngine = dockerEngine;
        fileLogger = new FilePipelingLogger(this.getJobLogPath());
    }

    static List<String> extraHostsList;

    List<String> getExtraHosts() {
        if (extraHostsList == null) {
            extraHostsList = Arrays.asList(extraHosts.split(","));
        }
        return extraHostsList;
    }

    @Override
    public void setUp() {
    }

    @Override
    public void exec() {
        // 跳过完成的
        this.pipeline.skipNodes();
        List<Pipeline.PipelineTask> pipelines =
            this.pipeline.getAvailablePipelineTasks();
        this.runTasks(pipelines);
    }

    private void runTasks(List<Pipeline.PipelineTask> taskList) {
        // 顺序执行
        for (Pipeline.PipelineTask task : taskList) {
            PipelineJobTaskLog jobTaskLog = this.executeTask(task);
            if (jobTaskLog.getExitedValue() == 0) {
                this.onPipelineTaskFinish(task, jobTaskLog);
            } else {
                this.onError(task, jobTaskLog);
            }
        }
    }

    private void onPipelineTaskFinish(Pipeline.PipelineTask task, PipelineJobTaskLog jobTaskLog) {
        this.jobRunner.onPipelineTaskFinish(jobTaskLog);
        jobTaskLog.appendLog("onFinishPipeline :" + task.getName());
        this.pipeline.onPipelineTaskFinish(task);
        if (this.pipeline.isFinished()) {
            System.out.println("pipeline finish");
            this.jobRunner.onFinish(this.jobId);
            return;
        }
        List<Pipeline.PipelineTask> pipelines = this.pipeline.getAvailablePipelineTasks();
        this.runTasks(pipelines);
    }


    private void onError(Pipeline.PipelineTask pipeline,
                         PipelineJobTaskLog jobTaskLog) {
        jobTaskLog.appendLog("proc \"" + pipeline.getName() + "\" error");
        jobTaskLog.appendLog("start to kill all pipeline task");
        jobTaskLog.appendLog("pipeline exit with error");
        this.jobRunner.onError(this.jobId, jobTaskLog);
    }

    private List<ContainerVolumeBind> convert2VolumeBinds(List<String> volumes) {
        if (volumes == null) {
            return null;
        }
        return volumes.stream().map(v -> {
            String[] parts = v.split(":", 2);
            return new ContainerVolumeBind(parts[0], parts[1]);
        }).collect(Collectors.toList());
    }

    private String[] convertCommands(List<String> commands) {
        if (commands == null) {
            return null;
        }
        return commands.toArray(new String[commands.size()]);
    }

    private PipelineJobTaskLog executeTask(Pipeline.PipelineTask task) {
        PipelineJobTaskLog log = PipelineJobTaskLog.builder()
            .id(UUIDUtils.getUUIDString()).taskName(task.getName())
            .jobId(this.jobId)
            .build();
        this.fileLogger.setWriterLogger(log);
        for (Pipeline.Step step : task.getSteps()) {
            try {
                this.fileLogger.appendLog("proc \"" + step.getName() + "\" started");
                // 启动容器
                this.startContainer(step);

                // 执行
                int exitCode = this.dockerEngine
                                    .runContainer(this.currentContainerId,
                                                  step.getName(), this.fileLogger);

                if (exitCode == 0) {
                    // 从容器读取exitCode
                    InspectContainerResponse response = this.dockerEngine
                        .getContainer(this.currentContainerId);
                    if (response != null) {
                        exitCode = response.getState().getExitCode();
                    }
                }

                log.setExitedValue(exitCode);

                // 清理容器
                this.dockerEngine.removeOldContainer(this.currentContainerId);
                this.currentContainerId = null;
                this.fileLogger.appendLog("proc \"" + step.getName() + "\" exited with status " + exitCode);

                if (exitCode != 0) {
                    log.setStatus(Status.FAIL);
                    return log;
                }
            } catch (Exception ex) {
                log.setStatus(Status.FAIL);
                this.fileLogger.appendLog("proc \"" + step.getName() + "\" catch ex " + ex.getMessage());
                this.fileLogger.appendLog("proc \"" + step.getName() + "\" exited with status -1");
                return log;
            }
        }

        log.setStatus(Status.FINISHED);
        return log;
    }

    File logFile;

    public File getJobLogPath() {
        if (logFile != null) {
            return logFile;
        }

        logFile =  Paths.get(this.config.getPipelineLogPath(),
                             this.getDateStr(),
                         this.jobId).toFile();

        if (!logFile.exists()) {
            try {
                FileUtils.forceMkdir(logFile);
            } catch (IOException e) {
            }
        }

        logFile =  Paths.get(this.config.getPipelineLogPath(),
                         this.getDateStr(),
                         this.jobId, "job.log").toFile();
        return logFile;
    }

    private String getDateStr() {
        return new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
    }

    private String getContainerName(Pipeline.Step step) {
        return this.jobId + "_" + step.getName();
    }

    private void startContainer(Pipeline.Step step) {
        //this.pullImage(step);
        this.currentContainerId = this.dockerEngine.startContainer(
            step.getImage(),
            this.getContainerName(step),
            null,
            this.convert2VolumeBinds(step.getVolumes()),
            null,
            step.getEnvironmentString(),
            step.getEntryPoint(),
            this.convertCommands(step.getCommand())
        );
    }

    private void pullImage(Pipeline.Step step) {
        try {
            // pull image
            this.dockerEngine.pullImage(step.getImage());
        } catch (Exception e) {

        }
    }

    @Override
    public void kill() {
        try {
            if (this.currentContainerId != null) {
                this.dockerEngine.removeOldContainer(this.currentContainerId);
            }
        } catch (Exception e) {
        }

        this.fileLogger.close();
    }
}
