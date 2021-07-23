package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.core.dto.BaseRequest;
import com.github.jadepeng.pipeline.service.dto.TagDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.Tag}.
 */
public interface TagService {
    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save.
     * @return the persisted entity.
     */
    TagDTO save(TagDTO tagDTO);

    /**
     * Partially updates a tag.
     *
     * @param tagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagDTO> partialUpdate(TagDTO tagDTO);

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagDTO> findAll(BaseRequest pageable);

    /**
     * Get the "id" tag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagDTO> findOne(String id);

    /**
     * Delete the "id" tag.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
