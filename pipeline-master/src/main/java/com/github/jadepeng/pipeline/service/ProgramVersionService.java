package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.service.dto.ProgramVersionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.ProgramVersion}.
 */
public interface ProgramVersionService {
    /**
     * Save a programVersion.
     *
     * @param programVersionDTO the entity to save.
     * @return the persisted entity.
     */
    ProgramVersionDTO save(ProgramVersionDTO programVersionDTO);

    /**
     * Partially updates a programVersion.
     *
     * @param programVersionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgramVersionDTO> partialUpdate(ProgramVersionDTO programVersionDTO);

    /**
     * Get all the programVersions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProgramVersionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" programVersion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgramVersionDTO> findOne(String id);

    /**
     * Delete the "id" programVersion.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
