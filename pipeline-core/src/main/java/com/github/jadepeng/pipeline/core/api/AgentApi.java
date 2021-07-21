package com.github.jadepeng.pipeline.core.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.jadepeng.pipeline.core.bean.AgentInfo;
import com.github.jadepeng.pipeline.core.bean.OperationalSystemInfo;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.dto.BaseResponse;
import com.github.jadepeng.pipeline.core.dto.ExecutePipelineRequest;
import com.github.jadepeng.pipeline.core.dto.JobState;

import feign.Headers;
import feign.RequestLine;

public interface AgentApi {

    @RequestLine(value = "GET /api/info")
    AgentInfo info();

    @RequestLine(value = "GET /api/os")
    OperationalSystemInfo os() ;

    @RequestLine("POST /api/runJob")
    @Headers("Content-Type: application/json")
    BaseResponse runJob(@Valid @RequestBody ExecutePipelineRequest job);

    @RequestLine("POST /api/stopJob")
    BaseResponse stopJob(@Valid @RequestBody PipelineJob job);
}
