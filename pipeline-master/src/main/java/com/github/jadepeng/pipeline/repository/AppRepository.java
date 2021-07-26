package com.github.jadepeng.pipeline.repository;

import com.github.jadepeng.pipeline.domain.App;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the App entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRepository extends MongoRepository<App, String> {}
