package com.github.jadepeng.pipeline.web.rest;

import com.github.jadepeng.pipeline.repository.DockerImageRepository;
import com.github.jadepeng.pipeline.service.DockerImageService;
import com.github.jadepeng.pipeline.service.dto.DockerImageDTO;
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
 * REST controller for managing {@link com.github.jadepeng.pipeline.domain.DockerImage}.
 */
@RestController
@RequestMapping("/api")
public class DockerImageResource {

    private final Logger log = LoggerFactory.getLogger(DockerImageResource.class);

    private static final String ENTITY_NAME = "dockerImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DockerImageService dockerImageService;

    private final DockerImageRepository dockerImageRepository;

    public DockerImageResource(DockerImageService dockerImageService, DockerImageRepository dockerImageRepository) {
        this.dockerImageService = dockerImageService;
        this.dockerImageRepository = dockerImageRepository;
    }

    /**
     * {@code POST  /docker-images} : Create a new dockerImage.
     *
     * @param dockerImageDTO the dockerImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dockerImageDTO, or with status {@code 400 (Bad Request)} if the dockerImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/docker-images")
    public ResponseEntity<DockerImageDTO> createDockerImage(@RequestBody DockerImageDTO dockerImageDTO) throws URISyntaxException {
        log.debug("REST request to save DockerImage : {}", dockerImageDTO);
        if (dockerImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new dockerImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DockerImageDTO result = dockerImageService.save(dockerImageDTO);
        return ResponseEntity
            .created(new URI("/api/docker-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /docker-images/:id} : Updates an existing dockerImage.
     *
     * @param id the id of the dockerImageDTO to save.
     * @param dockerImageDTO the dockerImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dockerImageDTO,
     * or with status {@code 400 (Bad Request)} if the dockerImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dockerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/docker-images/{id}")
    public ResponseEntity<DockerImageDTO> updateDockerImage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DockerImageDTO dockerImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DockerImage : {}, {}", id, dockerImageDTO);
        if (dockerImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dockerImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dockerImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DockerImageDTO result = dockerImageService.save(dockerImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dockerImageDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /docker-images/:id} : Partial updates given fields of an existing dockerImage, field will ignore if it is null
     *
     * @param id the id of the dockerImageDTO to save.
     * @param dockerImageDTO the dockerImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dockerImageDTO,
     * or with status {@code 400 (Bad Request)} if the dockerImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dockerImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dockerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/docker-images/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DockerImageDTO> partialUpdateDockerImage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DockerImageDTO dockerImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DockerImage partially : {}, {}", id, dockerImageDTO);
        if (dockerImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dockerImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dockerImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DockerImageDTO> result = dockerImageService.partialUpdate(dockerImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dockerImageDTO.getId())
        );
    }

    /**
     * {@code GET  /docker-images} : get all the dockerImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dockerImages in body.
     */
    @GetMapping("/docker-images")
    public ResponseEntity<List<DockerImageDTO>> getAllDockerImages(Pageable pageable) {
        log.debug("REST request to get a page of DockerImages");
        Page<DockerImageDTO> page = dockerImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /docker-images/:id} : get the "id" dockerImage.
     *
     * @param id the id of the dockerImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dockerImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/docker-images/{id}")
    public ResponseEntity<DockerImageDTO> getDockerImage(@PathVariable String id) {
        log.debug("REST request to get DockerImage : {}", id);
        Optional<DockerImageDTO> dockerImageDTO = dockerImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dockerImageDTO);
    }

    /**
     * {@code DELETE  /docker-images/:id} : delete the "id" dockerImage.
     *
     * @param id the id of the dockerImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/docker-images/{id}")
    public ResponseEntity<Void> deleteDockerImage(@PathVariable String id) {
        log.debug("REST request to delete DockerImage : {}", id);
        dockerImageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
