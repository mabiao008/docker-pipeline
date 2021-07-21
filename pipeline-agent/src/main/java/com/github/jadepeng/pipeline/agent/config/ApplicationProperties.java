package com.github.jadepeng.pipeline.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Properties specific to Agent.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    /**
     * 允许的最大任务
     */
    private Integer maxTasks = 20;

    /**
     * 最大内存
     */
    private Integer maxMemoryInGB = 128;

    /**
     * CPU 核数
     */
    private Integer maxCpuCores = 24;

    /**
     * agent label
     */
    private String labels;

    private String dockerServer = "172.31.161.38";

    private String dockerCertPath;

    private Boolean dockerTlsVerify = false;

    private String pipelineLogPath;
}
