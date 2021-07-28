package com.github.jadepeng.pipeline.agent.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.jadepeng.pipeline.agent.core.AgentContext;
import com.github.jadepeng.pipeline.agent.core.OperationalSystem;
import com.github.jadepeng.pipeline.agent.jobrunner.JobRunner;
import com.github.jadepeng.pipeline.core.api.AgentApi;
import com.github.jadepeng.pipeline.core.bean.AgentInfo;
import com.github.jadepeng.pipeline.core.bean.OperationalSystemInfo;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.PipelineJobLog;
import com.github.jadepeng.pipeline.core.dto.BasePayloadResponse;
import com.github.jadepeng.pipeline.core.dto.BaseResponse;
import com.github.jadepeng.pipeline.core.dto.ExecutePipelineRequest;

import io.micrometer.core.annotation.Timed;

/**
 * Agent 接口
 * @author jqpeng
 */
@RestController
@RequestMapping
public class AgentResource implements AgentApi {

    @Autowired
    AgentContext agentContext;

    @Autowired
    JobRunner jobRunner;

    @RequestMapping(value = "/api/os", method = RequestMethod.GET)
    @Override
    @Timed
    public OperationalSystemInfo os() {
        return OperationalSystem.info();
    }

    @RequestMapping(value = "/api/joblog/{jobId}", method = RequestMethod.GET)
    @Override
    @Timed
    public PipelineJobLog getJobLog(@PathVariable(value = "jobId") String jobId) {
        return jobRunner.getJobLog(jobId);
    }

    @RequestMapping(value = "/api/runJob", method = RequestMethod.POST)
    @Override
    public BaseResponse runJob(@Valid ExecutePipelineRequest request) {
        this.jobRunner.runJob(request);
        return BasePayloadResponse.success();
    }

    @RequestMapping(value = "/api/stopJob", method = RequestMethod.POST)
    @Override
    public BaseResponse stopJob(@Valid PipelineJob job) {
        this.jobRunner.stopJob(job.getId());
        return BasePayloadResponse.success();
    }

    @RequestMapping(value = "/api/info", method = RequestMethod.GET)
    @Override
    @Timed
    public AgentInfo info() {
        return agentContext.info();
    }

}
