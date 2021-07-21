package com.github.jadepeng.pipeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.jadepeng.pipeline.MQEvents;
import com.github.jadepeng.pipeline.config.ApplicationProperties;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.repository.PipelineJobRepository;
import com.github.jadepeng.pipeline.scheduler.MesssageProto;

/**
 * 任务提交
 *
 * @author jqpeng
 */
@Service
public class JobSubmiter {
    private final MQService mqService;
    private final PipelineJobRepository pipelineJobRepository;

    @Autowired
    public JobSubmiter(
        MQService mqService,
        PipelineJobRepository pipelineJobRepository) {
        this.mqService = mqService;
        this.pipelineJobRepository = pipelineJobRepository;
    }

    public boolean submitJob(PipelineJob pipelineJob) {
        try {
            this.mqService
                .sendMessage(MQEvents.SUBMIT_JOB, pipelineJob.getId());
            return true;
        } catch (Exception e) {
            pipelineJob.setStatus(Status.FAIL);
            pipelineJob.setRemark("提交任务出错,{}" + e.getMessage());
            this.pipelineJobRepository.save(pipelineJob);
        }
        return false;
    }

    public boolean cancelJob(PipelineJob pipelineJob) {
        try {
            this.mqService
                .sendMessage(MQEvents.CANCEL_JOB, pipelineJob.getId());
            return true;
        } catch (Exception e) {
            pipelineJob.setStatus(Status.FAIL);
            pipelineJob.setRemark("取消任务出错,{}" + e.getMessage());
            this.pipelineJobRepository.save(pipelineJob);
        }
        return false;
    }


}
