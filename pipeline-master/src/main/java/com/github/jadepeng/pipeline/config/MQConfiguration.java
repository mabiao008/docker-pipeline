package com.github.jadepeng.pipeline.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class MQConfiguration {
    private String producerGroup = "aimindProducer";
    private String consumerGroup = "aimindPushConsumer";
    private String topic = "aimind_new";
    private boolean enableProducer = true;
    private boolean enableConsumer = true;
}
