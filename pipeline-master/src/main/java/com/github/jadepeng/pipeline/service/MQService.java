package com.github.jadepeng.pipeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.jadepeng.pipeline.MQEvents;
import com.github.jadepeng.pipeline.config.ApplicationProperties;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.repository.PipelineJobRepository;
import com.github.jadepeng.pipeline.scheduler.MesssageProto;

/**
 * MQ
 * @author jqpeng
 */
@Service
public class MQService {

  private final KafkaTemplate kafkaTemplate;

  private final String topicName;

  @Autowired
  public MQService(KafkaTemplate kafkaTemplate,
                   ApplicationProperties appConfig,
                   PipelineJobRepository pipelineJobRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.topicName = appConfig.getMq().getTopic();
  }


  public void sendMessage(String tags, String body) {
    byte[] data = MesssageProto.Message.newBuilder()
                                       .setType(tags).setMessage(body)
                                       .build().toByteArray();
    this.kafkaTemplate.send(this.topicName, data);
  }

}
