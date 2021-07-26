package com.github.jadepeng.pipeline.web.rest;

import com.github.jadepeng.pipeline.repository.AppRepository;
import com.github.jadepeng.pipeline.service.AppService;
import com.github.jadepeng.pipeline.service.dto.AppDTO;
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
 * REST controller for managing {@link com.github.jadepeng.pipeline.domain.App}.
 */
@RestController
@RequestMapping("/api")
public class AppResource {

    private final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "app";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppService appService;

    private final AppRepository appRepository;

    public AppResource(AppService appService, AppRepository appRepository) {
        this.appService = appService;
        this.appRepository = appRepository;
    }

    /**
     * {@code POST  /apps} : Create a new app.
     *
     * @param appDTO the appDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appDTO, or with status {@code 400 (Bad Request)} if the app has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apps")
    public ResponseEntity<AppDTO> createApp(@RequestBody AppDTO appDTO) throws URISyntaxException {
        log.debug("REST request to save App : {}", appDTO);
        if (appDTO.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppDTO result = appService.save(appDTO);
        return ResponseEntity
            .created(new URI("/api/apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /apps/:id} : Updates an existing app.
     *
     * @param id the id of the appDTO to save.
     * @param appDTO the appDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDTO,
     * or with status {@code 400 (Bad Request)} if the appDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apps/{id}")
    public ResponseEntity<AppDTO> updateApp(@PathVariable(value = "id", required = false) final String id, @RequestBody AppDTO appDTO)
        throws URISyntaxException {
        log.debug("REST request to update App : {}, {}", id, appDTO);
        if (appDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppDTO result = appService.save(appDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /apps/:id} : Partial updates given fields of an existing app, field will ignore if it is null
     *
     * @param id the id of the appDTO to save.
     * @param appDTO the appDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDTO,
     * or with status {@code 400 (Bad Request)} if the appDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppDTO> partialUpdateApp(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AppDTO appDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update App partially : {}, {}", id, appDTO);
        if (appDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppDTO> result = appService.partialUpdate(appDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDTO.getId()));
    }

    /**
     * {@code GET  /apps} : get all the apps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apps in body.
     */
    @GetMapping("/apps")
    public ResponseEntity<List<AppDTO>> getAllApps(Pageable pageable) {
        log.debug("REST request to get a page of Apps");
        Page<AppDTO> page = appService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apps/:id} : get the "id" app.
     *
     * @param id the id of the appDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apps/{id}")
    public ResponseEntity<AppDTO> getApp(@PathVariable String id) {
        log.debug("REST request to get App : {}", id);
        Optional<AppDTO> appDTO = appService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appDTO);
    }

    /**
     * {@code DELETE  /apps/:id} : delete the "id" app.
     *
     * @param id the id of the appDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apps/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable String id) {
        log.debug("REST request to delete App : {}", id);
        appService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
