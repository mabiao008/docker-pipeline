package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.service.dto.ProgramArgumentsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.ProgramArguments}.
 */
public interface ProgramArgumentsService {
    /**
     * Save a programArguments.
     *
     * @param programArgumentsDTO the entity to save.
     * @return the persisted entity.
     */
    ProgramArgumentsDTO save(ProgramArgumentsDTO programArgumentsDTO);

    /**
     * Partially updates a programArguments.
     *
     * @param programArgumentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgramArgumentsDTO> partialUpdate(ProgramArgumentsDTO programArgumentsDTO);

    /**
     * Get all the programArguments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProgramArgumentsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" programArguments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgramArgumentsDTO> findOne(String id);

    /**
     * Delete the "id" programArguments.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
