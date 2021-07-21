package com.github.jadepeng.pipeline.core.api;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;
import com.github.jadepeng.pipeline.core.dto.*;

@FeignClient(name = "MASTER", path = "/api/pipelines")
public interface MasterApi {

    @RequestMapping(value = "/job/stateChange", method = RequestMethod.POST)
    BaseResponse jobStateChange(@Valid @RequestBody JobState state);

    @RequestMapping(value = "/job/log", method = RequestMethod.POST)
    BaseResponse saveJobTaskLog(@Valid @RequestBody PipelineJobTaskLog log);
}
