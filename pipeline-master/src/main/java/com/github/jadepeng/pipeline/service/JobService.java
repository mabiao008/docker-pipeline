package com.github.jadepeng.pipeline.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.Status;
import com.github.jadepeng.pipeline.repository.PipelineJobRepository;
import com.github.jadepeng.pipeline.repository.PipelineRepository;

@Service
public class JobService {

  private final PipelineRepository pipelineRepository;
  private final PipelineJobRepository pipelineJobRepository;
  private final MongoTemplate mongoTemplate;
  private final JobSubmiter submiter;

  @Autowired
  public JobService(PipelineRepository pipelineRepository,
                    PipelineJobRepository pipelineJobRepository,
                    MongoTemplate mongoTemplate,
                    JobSubmiter submiter) {
    this.pipelineRepository = pipelineRepository;
    this.pipelineJobRepository = pipelineJobRepository;
    this.mongoTemplate = mongoTemplate;
    this.submiter = submiter;
  }

  public PipelineJob runPipeline(Pipeline pipeline) {
    PipelineJob job = PipelineJob
      .builder()
      .pipelineId(pipeline.getId())
      .pipelineName(pipeline.getName())
      .id(UUID.randomUUID().toString())
      .startTime(System.currentTimeMillis())
      .tasks(pipeline.getPipelineTasks())
      .status(Status.QUEUE)
      .build();

    this.pipelineJobRepository.save(job);

    this.submiter.submitJob(job);

    return job;
  }

  public boolean cancelJob(PipelineJob job) {
    return this.submiter.cancelJob(job);
  }
}
