package com.github.jadepeng.pipeline.service;

import com.github.jadepeng.pipeline.service.dto.ProgramDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.jadepeng.pipeline.domain.Program}.
 */
public interface ProgramService {
    /**
     * Save a program.
     *
     * @param programDTO the entity to save.
     * @return the persisted entity.
     */
    ProgramDTO save(ProgramDTO programDTO);

    /**
     * Partially updates a program.
     *
     * @param programDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgramDTO> partialUpdate(ProgramDTO programDTO);

    /**
     * Get all the programs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProgramDTO> findAll(Pageable pageable);

    /**
     * Get the "id" program.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgramDTO> findOne(String id);

    /**
     * Delete the "id" program.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
