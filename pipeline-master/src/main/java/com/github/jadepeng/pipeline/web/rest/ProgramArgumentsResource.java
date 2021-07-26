package com.github.jadepeng.pipeline.web.rest;

import com.github.jadepeng.pipeline.repository.ProgramArgumentsRepository;
import com.github.jadepeng.pipeline.service.ProgramArgumentsService;
import com.github.jadepeng.pipeline.service.dto.ProgramArgumentsDTO;
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
 * REST controller for managing {@link com.github.jadepeng.pipeline.domain.ProgramArguments}.
 */
@RestController
@RequestMapping("/api")
public class ProgramArgumentsResource {

    private final Logger log = LoggerFactory.getLogger(ProgramArgumentsResource.class);

    private static final String ENTITY_NAME = "programArguments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramArgumentsService programArgumentsService;

    private final ProgramArgumentsRepository programArgumentsRepository;

    public ProgramArgumentsResource(
        ProgramArgumentsService programArgumentsService,
        ProgramArgumentsRepository programArgumentsRepository
    ) {
        this.programArgumentsService = programArgumentsService;
        this.programArgumentsRepository = programArgumentsRepository;
    }

    /**
     * {@code POST  /program-arguments} : Create a new programArguments.
     *
     * @param programArgumentsDTO the programArgumentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programArgumentsDTO, or with status {@code 400 (Bad Request)} if the programArguments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/program-arguments")
    public ResponseEntity<ProgramArgumentsDTO> createProgramArguments(@RequestBody ProgramArgumentsDTO programArgumentsDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProgramArguments : {}", programArgumentsDTO);
        if (programArgumentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new programArguments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgramArgumentsDTO result = programArgumentsService.save(programArgumentsDTO);
        return ResponseEntity
            .created(new URI("/api/program-arguments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /program-arguments/:id} : Updates an existing programArguments.
     *
     * @param id the id of the programArgumentsDTO to save.
     * @param programArgumentsDTO the programArgumentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programArgumentsDTO,
     * or with status {@code 400 (Bad Request)} if the programArgumentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programArgumentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/program-arguments/{id}")
    public ResponseEntity<ProgramArgumentsDTO> updateProgramArguments(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProgramArgumentsDTO programArgumentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProgramArguments : {}, {}", id, programArgumentsDTO);
        if (programArgumentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programArgumentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programArgumentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProgramArgumentsDTO result = programArgumentsService.save(programArgumentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programArgumentsDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /program-arguments/:id} : Partial updates given fields of an existing programArguments, field will ignore if it is null
     *
     * @param id the id of the programArgumentsDTO to save.
     * @param programArgumentsDTO the programArgumentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programArgumentsDTO,
     * or with status {@code 400 (Bad Request)} if the programArgumentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programArgumentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programArgumentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/program-arguments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProgramArgumentsDTO> partialUpdateProgramArguments(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProgramArgumentsDTO programArgumentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProgramArguments partially : {}, {}", id, programArgumentsDTO);
        if (programArgumentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programArgumentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programArgumentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgramArgumentsDTO> result = programArgumentsService.partialUpdate(programArgumentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programArgumentsDTO.getId())
        );
    }

    /**
     * {@code GET  /program-arguments} : get all the programArguments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programArguments in body.
     */
    @GetMapping("/program-arguments")
    public ResponseEntity<List<ProgramArgumentsDTO>> getAllProgramArguments(Pageable pageable) {
        log.debug("REST request to get a page of ProgramArguments");
        Page<ProgramArgumentsDTO> page = programArgumentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /program-arguments/:id} : get the "id" programArguments.
     *
     * @param id the id of the programArgumentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programArgumentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/program-arguments/{id}")
    public ResponseEntity<ProgramArgumentsDTO> getProgramArguments(@PathVariable String id) {
        log.debug("REST request to get ProgramArguments : {}", id);
        Optional<ProgramArgumentsDTO> programArgumentsDTO = programArgumentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programArgumentsDTO);
    }

    /**
     * {@code DELETE  /program-arguments/:id} : delete the "id" programArguments.
     *
     * @param id the id of the programArgumentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/program-arguments/{id}")
    public ResponseEntity<Void> deleteProgramArguments(@PathVariable String id) {
        log.debug("REST request to delete ProgramArguments : {}", id);
        programArgumentsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
