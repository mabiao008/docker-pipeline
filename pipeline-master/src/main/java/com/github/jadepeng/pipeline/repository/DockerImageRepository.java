package com.github.jadepeng.pipeline.repository;

import com.github.jadepeng.pipeline.domain.DockerImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DockerImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DockerImageRepository extends MongoRepository<DockerImage, String> {}
