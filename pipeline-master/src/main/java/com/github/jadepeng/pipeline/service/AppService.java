package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.service.dto.AppDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.App}.
 */
public interface AppService {
    /**
     * Save a app.
     *
     * @param appDTO the entity to save.
     * @return the persisted entity.
     */
    AppDTO save(AppDTO appDTO);

    /**
     * Partially updates a app.
     *
     * @param appDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppDTO> partialUpdate(AppDTO appDTO);

    /**
     * Get all the apps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppDTO> findAll(Pageable pageable);

    /**
     * Get the "id" app.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppDTO> findOne(String id);

    /**
     * Delete the "id" app.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
