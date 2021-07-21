package com.github.jadepeng.pipeline.core.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.jadepeng.pipeline.core.PipelineLogger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jqpeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pipeline-job-task-log")
public class PipelineJobTaskLog implements PipelineLogger {
    @Id
    private String id;
    @Indexed
    @NotNull
    private String jobId;
    private Status status;
    private String taskName;
    private int exitedValue;
    private List<String> logs;

    @Override
    public void appendLog(String log) {
        if (this.logs == null) {
            this.logs = new ArrayList<>();
        }
        this.logs.add(log);
    }
}
