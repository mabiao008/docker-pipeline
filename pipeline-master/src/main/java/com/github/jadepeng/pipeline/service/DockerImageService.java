package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.service.dto.DockerImageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.DockerImage}.
 */
public interface DockerImageService {
    /**
     * Save a dockerImage.
     *
     * @param dockerImageDTO the entity to save.
     * @return the persisted entity.
     */
    DockerImageDTO save(DockerImageDTO dockerImageDTO);

    /**
     * Partially updates a dockerImage.
     *
     * @param dockerImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DockerImageDTO> partialUpdate(DockerImageDTO dockerImageDTO);

    /**
     * Get all the dockerImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DockerImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dockerImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DockerImageDTO> findOne(String id);

    /**
     * Delete the "id" dockerImage.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
