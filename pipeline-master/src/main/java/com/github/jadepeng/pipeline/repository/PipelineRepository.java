package com.github.jadepeng.pipeline.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.jadepeng.pipeline.core.bean.Pipeline;

@Repository
public interface PipelineRepository extends MongoRepository<Pipeline, String> {

  List<Pipeline> findAllByNameAndIsAvailable(String name, Integer isAvailable);

  List<Pipeline> findAllByNameLikeAndIsAvailable(String name, Integer isAvailable);

  Page<Pipeline> findAllByIsAvailableOrderByCreatedDateDesc(Integer isAvailable, Pageable pageable);

  Page<Pipeline> findAllByNameLikeAndIsAvailableOrderByCreatedDateDesc(String name, Integer isAvailable, Pageable pageable);

  List<Pipeline> findAllByCreatedDateBetweenAndIsAvailableOrderByCreatedDateAsc(
    LocalDateTime from, LocalDateTime to, Integer isAvailable);

}
