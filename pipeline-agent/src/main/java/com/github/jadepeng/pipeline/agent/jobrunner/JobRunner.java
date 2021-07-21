package com.github.jadepeng.pipeline.agent.jobrunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.github.jadepeng.pipeline.agent.config.ApplicationProperties;
import com.github.jadepeng.pipeline.agent.core.AgentContext;
import com.github.jadepeng.pipeline.agent.jobrunner.docker.DockerEngine;
import com.github.jadepeng.pipeline.core.api.MasterApi;
import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.core.dto.ExecutePipelineRequest;
import com.github.jadepeng.pipeline.core.dto.JobState;

import lombok.Data;
import lombok.SneakyThrows;

@Component
public class JobRunner {

    private final MasterApi masterApi;
    private final ExecutorService executor;
    private final AgentContext agentContext;
    private final ApplicationProperties config;
    private final Map<String, JobContext> jobContexts = new HashMap<String,
        JobContext>();
    private final DockerEngine dockerEngine;


    public JobRunner(MasterApi masterApi,
                     ApplicationProperties configuration,
                     AgentContext agentContext,
                     ApplicationProperties config,
                     DockerEngine dockerEngine) {

        this.masterApi = masterApi;
        this.executor = new ThreadPoolExecutor(configuration.getMaxTasks(),
                                               configuration.getMaxTasks(),
                                               0L, TimeUnit.MILLISECONDS,
                                               new LinkedBlockingQueue<>());
        this.agentContext = agentContext;
        this.config = config;
        this.dockerEngine = dockerEngine;
    }

    @SneakyThrows
    public void runJob(ExecutePipelineRequest request) {
        this.agentContext.incrementRunTaskCount();

        DockerRunnerEngine engine = new DockerRunnerEngine(this, request,
                                                           this.config,
                                                           this.dockerEngine);
        engine.setUp();

        Thread workThread = new Thread(() -> engine.exec());
        this.executor.execute(workThread);

        JobContext context = new JobContext();
        context.setRunner(engine);
        context.setWorkingThread(workThread);

        this.jobContexts.put(request.getPipelineJobId(), context);

        this.jobStarted(request.getPipelineJobId());
    }

    public void stopJob(String jobId) {
        this.agentContext.decrementRunningTaskCount();
        this.cleanContext(jobId);
    }

    public void onFinish(String jobId) {
        this.jobSuccess(jobId);
        this.agentContext.decrementRunningTaskCount();
        this.cleanContext(jobId);
    }

    public void onPipelineTaskFinish(PipelineJobTaskLog jobTaskLog) {
        this.masterApi.saveJobTaskLog(jobTaskLog);
    }

    public void onError(String jobId, PipelineJobTaskLog jobTaskLog) {
        this.masterApi.saveJobTaskLog(jobTaskLog);
        this.jobFailed(jobId, jobTaskLog);
        this.agentContext.decrementRunningTaskCount();
        this.cleanContext(jobId);
    }

    private void cleanContext(String jobId) {
        try {
            JobContext context = this.jobContexts.remove(jobId);
            context.clean();
        } catch (Exception e) {
        }
    }

    private void jobSuccess(String jobId) {
        JobState state = JobState.builder().status(Status.FINISHED)
                                 .exitedValue(0)
                                 .jobId(jobId)
                                 .timestamp(System.currentTimeMillis())
                                 .build();
        this.masterApi.jobStateChange(state);
    }

    private void jobFailed(String jobId, PipelineJobTaskLog jobTaskLog) {
        JobState state = JobState.builder().status(jobTaskLog.getStatus())
                                 .exitedValue(jobTaskLog.getExitedValue())
                                 .timestamp(System.currentTimeMillis())
                                 .currentTask(jobTaskLog.getTaskName())
                                 .jobId(jobId)
                                 .build();
        this.masterApi.jobStateChange(state);
    }

    private void jobStarted(String jobId) {
        JobState state = JobState.builder().status(Status.RUNNING)
                                 .exitedValue(0)
                                 .jobId(jobId)
                                 .timestamp(System.currentTimeMillis())
                                 .build();
        this.masterApi.jobStateChange(state);
    }

    @Data
    private static class JobContext {
        private DockerRunnerEngine runner;
        private Thread workingThread;
        private String logPath;

        public void clean() {
            try {
                this.workingThread.interrupt();
            } catch (Exception e) {
            }
            runner.kill();
            this.runner = null;
        }
    }
}
