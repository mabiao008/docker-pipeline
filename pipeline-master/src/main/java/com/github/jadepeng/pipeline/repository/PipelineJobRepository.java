package com.github.jadepeng.pipeline.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.jadepeng.pipeline.core.bean.Pipeline;
import com.github.jadepeng.pipeline.core.bean.PipelineJob;
import com.github.jadepeng.pipeline.core.bean.Status;

/**
 *
 * Spring Data MongoDB repository for the PipelineJob entity.
 * @author jqpeng
 */
@Repository
public interface PipelineJobRepository extends
                                       MongoRepository<PipelineJob, String> {

  List<PipelineJob> findByStatus(Status status);

  PipelineJob findByPipelineIdAndStatus(String pipelineId, Status status);
}
