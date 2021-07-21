package com.github.jadepeng.pipeline.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.jadepeng.pipeline.core.bean.PipelineJobTaskLog;

@Repository
public interface PipelineJobTaskLogRepository extends MongoRepository<PipelineJobTaskLog, String> {
  List<PipelineJobTaskLog> findAllByJobId(String jobId);
}
