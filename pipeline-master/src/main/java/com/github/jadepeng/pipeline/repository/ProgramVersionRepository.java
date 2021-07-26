package com.github.jadepeng.pipeline.repository;

import com.github.jadepeng.pipeline.domain.ProgramVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProgramVersion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramVersionRepository extends MongoRepository<ProgramVersion, String> {}
