package com.github.jadepeng.pipeline.web.rest;

import java.util.Collection;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.jadepeng.pipeline.core.bean.AgentInfo;
import com.github.jadepeng.pipeline.core.dto.BasePayloadResponse;
import com.github.jadepeng.pipeline.core.dto.BaseRequest;
import com.github.jadepeng.pipeline.task.AgentDiscover;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/agent")
public class AgentResouce {
    private final Logger log = LoggerFactory.getLogger(AgentResouce.class);

    private final AgentDiscover agentDiscover;

    public AgentResouce(
        AgentDiscover agentDiscover) {
        this.agentDiscover = agentDiscover;
    }

    @GetMapping("/")
    @ResponseBody
    @ApiOperation(value = "查询", httpMethod = "GET")
    public BasePayloadResponse<Collection<AgentInfo>> getAllAgents(
        @Valid BaseRequest request) {
        log.debug("REST request to get all Pipelines");
        return BasePayloadResponse
            .success(agentDiscover.getAliveAgentInfo());
    }
}
