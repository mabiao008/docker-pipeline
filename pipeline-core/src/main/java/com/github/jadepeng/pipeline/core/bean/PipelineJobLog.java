package com.github.jadepeng.pipeline.core.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PipelineJobLog {
    private String currentTask;
    private List<PipelineJobTaskLog> logs;
    private int exitedValue;
    private Status status;
    private Long pipelineJobSt;
    private Long pipelineJobFt;

    public static PipelineJobLog fromLogs(
        Pipeline job,
        List<String> rawLogs,
        List<Pipeline.PipelineTask> sortedTask) {
        PipelineJobLog jobLog = new PipelineJobLog();
        jobLog.setLogs(new ArrayList<>());
        if (sortedTask != null && sortedTask.size() > 0 && rawLogs != null && rawLogs.size() > 0) {
            Map<String, PipelineJobTaskLog> taskId2Log = new HashMap<>();
            // pipeline_task_019dc5ed384a4fb082aa10d195102d61_5:
            //  proc "pipeline_task_019dc5ed384a4fb082aa10d195102d61_5" started
            boolean hasError = false;
            for (String log : rawLogs) {
                if (log.startsWith("proc \"pipeline")) {
                    String taskId = log.substring(log.indexOf("\"") + 1, log.lastIndexOf("\""));
                    int index = Integer.parseInt(taskId.substring(taskId.lastIndexOf("_") + 1)) - 1;
                    Pipeline.PipelineTask task = sortedTask.get(index);
                    if (log.endsWith("started")) {
                        // 获取index
                        PipelineJobTaskLog currentLog = new PipelineJobTaskLog();
                        currentLog.setTaskName(task.getName());
                        currentLog.setJobId(job.getId());
                        taskId2Log.put(taskId, currentLog);
                        jobLog.logs.add(currentLog);
                    } else if (log.contains("exited with status")) {
                        int exitValue = Integer.parseInt(log.substring(log.lastIndexOf("status") + "status".length() + 1));
                        PipelineJobTaskLog currentLog = taskId2Log.getOrDefault(taskId, null);
                        if (currentLog != null) {
                            currentLog.setExitedValue(exitValue);
                            currentLog.setStatus(exitValue == 0 ? Status.SUCCESS : Status.FAIL);
                            if (exitValue != 0) {
                                hasError = true;
                            }
                        }
                    }
                } else if (log.startsWith("pipeline_task_")) {
                    //
                    String taskId = log.substring(0, log.indexOf(":"));
                    PipelineJobTaskLog currentLog = taskId2Log.getOrDefault(taskId, null);
                    if (currentLog != null) {
                        currentLog.appendLog(log.substring(log.indexOf(":") + 1));
                    }
                }
            }
            if (hasError) {
                jobLog.setStatus(Status.FAIL);
                jobLog.setExitedValue(jobLog.getLogs().stream().filter(l -> l.getStatus() == Status.FAIL).findFirst().get().getExitedValue());
            } else {
                // 没有错误
                if (jobLog.getLogs().stream().filter(l -> l.getStatus() == Status.SUCCESS).count() == sortedTask.size()) {
                    jobLog.setStatus(Status.SUCCESS);
                } else {
                    jobLog.setStatus(Status.RUNNING);
                }
            }
//            jobLog.setExitedValue(currenLog.getExitedValue());
//            jobLog.setCurrentTask(currenLog.getPipelineTaskName());
        }
        return jobLog;
    }
}

