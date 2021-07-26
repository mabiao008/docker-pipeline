package com.github.jadepeng.pipeline.web.rest;

import com.github.jadepeng.pipeline.repository.ProgramVersionRepository;
import com.github.jadepeng.pipeline.service.ProgramVersionService;
import com.github.jadepeng.pipeline.service.dto.ProgramVersionDTO;
import com.github.jadepeng.pipeline.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.jadepeng.pipeline.domain.ProgramVersion}.
 */
@RestController
@RequestMapping("/api")
public class ProgramVersionResource {

    private final Logger log = LoggerFactory.getLogger(ProgramVersionResource.class);

    private static final String ENTITY_NAME = "programVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramVersionService programVersionService;

    private final ProgramVersionRepository programVersionRepository;

    public ProgramVersionResource(ProgramVersionService programVersionService, ProgramVersionRepository programVersionRepository) {
        this.programVersionService = programVersionService;
        this.programVersionRepository = programVersionRepository;
    }

    /**
     * {@code POST  /program-versions} : Create a new programVersion.
     *
     * @param programVersionDTO the programVersionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programVersionDTO, or with status {@code 400 (Bad Request)} if the programVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/program-versions")
    public ResponseEntity<ProgramVersionDTO> createProgramVersion(@RequestBody ProgramVersionDTO programVersionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProgramVersion : {}", programVersionDTO);
        if (programVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new programVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgramVersionDTO result = programVersionService.save(programVersionDTO);
        return ResponseEntity
            .created(new URI("/api/program-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /program-versions/:id} : Updates an existing programVersion.
     *
     * @param id the id of the programVersionDTO to save.
     * @param programVersionDTO the programVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programVersionDTO,
     * or with status {@code 400 (Bad Request)} if the programVersionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/program-versions/{id}")
    public ResponseEntity<ProgramVersionDTO> updateProgramVersion(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProgramVersionDTO programVersionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProgramVersion : {}, {}", id, programVersionDTO);
        if (programVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProgramVersionDTO result = programVersionService.save(programVersionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programVersionDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /program-versions/:id} : Partial updates given fields of an existing programVersion, field will ignore if it is null
     *
     * @param id the id of the programVersionDTO to save.
     * @param programVersionDTO the programVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programVersionDTO,
     * or with status {@code 400 (Bad Request)} if the programVersionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programVersionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/program-versions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProgramVersionDTO> partialUpdateProgramVersion(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProgramVersionDTO programVersionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProgramVersion partially : {}, {}", id, programVersionDTO);
        if (programVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgramVersionDTO> result = programVersionService.partialUpdate(programVersionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programVersionDTO.getId())
        );
    }

    /**
     * {@code GET  /program-versions} : get all the programVersions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programVersions in body.
     */
    @GetMapping("/program-versions")
    public ResponseEntity<List<ProgramVersionDTO>> getAllProgramVersions(Pageable pageable) {
        log.debug("REST request to get a page of ProgramVersions");
        Page<ProgramVersionDTO> page = programVersionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /program-versions/:id} : get the "id" programVersion.
     *
     * @param id the id of the programVersionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programVersionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/program-versions/{id}")
    public ResponseEntity<ProgramVersionDTO> getProgramVersion(@PathVariable String id) {
        log.debug("REST request to get ProgramVersion : {}", id);
        Optional<ProgramVersionDTO> programVersionDTO = programVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programVersionDTO);
    }

    /**
     * {@code DELETE  /program-versions/:id} : delete the "id" programVersion.
     *
     * @param id the id of the programVersionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/program-versions/{id}")
    public ResponseEntity<Void> deleteProgramVersion(@PathVariable String id) {
        log.debug("REST request to delete ProgramVersion : {}", id);
        programVersionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
