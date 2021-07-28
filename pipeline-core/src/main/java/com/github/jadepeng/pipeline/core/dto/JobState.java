package com.github.jadepeng.pipeline.core.dto;

import javax.validation.constraints.NotNull;

import com.github.jadepeng.pipeline.core.bean.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobState {
    @NotNull
    private String jobId;
    private Status status;
    private String currentTask;
    private int exitedValue;
    private long timestamp;
    private String logPath;
}
