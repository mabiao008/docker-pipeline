package com.github.jadepeng.pipeline.repository;

import com.github.jadepeng.pipeline.domain.ProgramArguments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProgramArguments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramArgumentsRepository extends MongoRepository<ProgramArguments, String> {}
