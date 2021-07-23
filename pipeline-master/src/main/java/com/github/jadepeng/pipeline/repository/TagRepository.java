package com.github.jadepeng.pipeline.repository;

import java.util.List;

import com.github.jadepeng.pipeline.domain.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Page<Tag> findAllByNameLike(String name, Pageable page);
}
