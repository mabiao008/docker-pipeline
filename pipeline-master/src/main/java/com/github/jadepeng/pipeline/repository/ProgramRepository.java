package com.github.jadepeng.pipeline.repository;

import com.github.jadepeng.pipeline.domain.Program;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Program entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramRepository extends MongoRepository<Program, String> {}
