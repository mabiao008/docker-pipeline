package com.github.jadepeng.pipeline.core.bean;

import lombok.Builder;
import lombok.Data;

/**
 * Agent 信息
 * @author jqpeng
 */
@Data
@Builder
public class AgentInfo implements Comparable<AgentInfo> {

    private String agentId;

    private String agentUrl;

    /**
     * 允许的最大任务
     */
    private Integer runningTaskCount = 0;

    private Integer availableTaskCount;

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

    @Override
    public int compareTo(AgentInfo o) {
        // desc 倒排
        return o.getAvailableTaskCount().compareTo(this.getAvailableTaskCount());
    }
}
