package com.github.jadepeng.pipeline.core.dto;

import com.github.jadepeng.pipeline.core.bean.Pipeline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutePipelineRequest {
    private Pipeline pipeline;
    private String pipelineJobId;
}
