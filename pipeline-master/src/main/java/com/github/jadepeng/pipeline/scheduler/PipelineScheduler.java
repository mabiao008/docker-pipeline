package com.github.jadepeng.pipeline.scheduler;

import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.jadepeng.pipeline.MQEvents;
import com.github.jadepeng.pipeline.core.api.AgentApi;
import com.github.jadepeng.pipeline.core.bean.AgentInfo;
import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.core.dto.ExecutePipelineRequest;
import com.github.jadepeng.pipeline.service.MQService;
import com.github.jadepeng.pipeline.service.PipelineService;
import com.github.jadepeng.pipeline.task.AgentDiscover;
import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

import lombok.SneakyThrows;

/**
 * 任务调度：
 * 分布式方案，任务放入MQ，统一调度
 * 服务挂了以后，先加载未完成任务放入MQ（或者）
 * 调度任务时，检查任务是否已执行，已执行的跳过
 */
@Component
public class PipelineScheduler {

    private static final Logger log =
        LoggerFactory.getLogger(PipelineScheduler.class);

    private final MQService mqService;
    private final AgentDiscover agentDiscover;
    private final PipelineService pipelineService;

    public PipelineScheduler(
        MQService mqService,
        AgentDiscover agentDiscover,
        PipelineService pipelineService) {
        this.mqService = mqService;
        this.agentDiscover = agentDiscover;
        this.pipelineService = pipelineService;
    }


    @KafkaListener(topics = "${application.mq.topic}")
    public void processMessage(ConsumerRecord<String, byte[]> record) {
        try {
            // 当前顺序调度，不区分队列，优先级
            MesssageProto.Message message =
                MesssageProto.Message.parseFrom(record.value());
            log.info("receive mq message {}", message.getType());
            String jobId = message.getMessage();
            try {
                switch (message.getType()) {
                    case MQEvents.SUBMIT_JOB:
                        this.scheduleJob(jobId);
                        break;
                    case MQEvents.CANCEL_JOB:
                        this.cancelJob(jobId);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("process message(type:{},jobid:{}) error, put it " +
                          "to mq again", message.getType(), jobId);
                // 重新放入mq
                this.mqService.sendMessage(message.getType(), jobId);
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("consumer mq message error", e);
        } catch (Exception e) {
            log.error("process mq message error", e);
        }
    }


    public void cancelJob(String jobId) {

    }

    public void scheduleJob(String jobId) {
        log.info("start schedule job #{} ", jobId);
        this.doScheduleJob(jobId);
        log.info("schedule job #{} done !", jobId);
    }

    @SneakyThrows
    private void doScheduleJob(String jobId) {
        // 挑选 agent

        List<AgentInfo> agents =
            Lists.newArrayList(
                this.agentDiscover.getAliveAgentInfo().iterator());
        // 分配
        if (agents.size() == 0) {
            log.info("there is no alived agent, waiting 5s");
            Thread.sleep(5000);
            doScheduleJob(jobId);
            return;
        }

        Collections.sort(agents);

        AgentInfo agent = agents.get(0);

        if (agent.getAvailableTaskCount() == 0) {
            log.info("the agent task queqe is full, waiting 5s");
            Thread.sleep(5000);
            doScheduleJob(jobId);
            return;
        }

        PipelineJob job = this.pipelineService.getPipelineJob(jobId);
        if (job == null) {
            return;
        }

        if (job.getStatus() != Status.QUEUE) {
            return;
        }

        Pipeline pipeline =
            this.pipelineService.getPipeline(job.getPipelineId());

        AgentApi api = this.agentDiscover.getAgentApi(agent.getAgentUrl());
        api.runJob(ExecutePipelineRequest.builder()
                                         .pipelineJobId(jobId)
                                         .pipeline(pipeline).build());
    }

}
