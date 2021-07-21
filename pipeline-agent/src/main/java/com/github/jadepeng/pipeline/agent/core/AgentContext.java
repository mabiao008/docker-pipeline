package com.github.jadepeng.pipeline.agent.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.jadepeng.pipeline.agent.config.ApplicationProperties;
import com.github.jadepeng.pipeline.core.bean.AgentInfo;


@Component
public class AgentContext {

    private static Logger logger = LoggerFactory.getLogger(AgentContext.class);

    private final ApplicationProperties configuration;

    /**
     * 允许的最大任务
     */
    private Integer runningTaskCount = 0;

    /**
     * 最大内存
     */
    private Integer usedMemoryInGB = 0;

    /**
     * CPU 核数
     */
    private Integer usedCpuCores = 0;

    /**
     * agent label
     */
    private String labels;

    public void incrementRunTaskCount() {
        this.runningTaskCount++;
    }

    public void decrementRunningTaskCount() {
        this.runningTaskCount--;
    }

    @Autowired
    public AgentContext(ApplicationProperties configuration) {
        this.configuration = configuration;
        this.labels = configuration.getLabels();
    }

    public AgentInfo info() {
        return AgentInfo.builder()
                        .labels(this.labels)
                        .availableTaskCount(configuration.getMaxTasks() - this.runningTaskCount)
                        .usedCpuCores(this.usedCpuCores)
                        .runningTaskCount(this.runningTaskCount)
                        .usedMemoryInGB(this.usedMemoryInGB)
                        .build();
    }

}
