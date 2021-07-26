package com.github.jadepeng.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.jadepeng.pipeline.IntegrationTest;
import com.github.jadepeng.pipeline.domain.DockerImage;
import com.github.jadepeng.pipeline.repository.DockerImageRepository;
import com.github.jadepeng.pipeline.service.dto.DockerImageDTO;
import com.github.jadepeng.pipeline.service.mapper.DockerImageMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link DockerImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DockerImageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/docker-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DockerImageRepository dockerImageRepository;

    @Autowired
    private DockerImageMapper dockerImageMapper;

    @Autowired
    private MockMvc restDockerImageMockMvc;

    private DockerImage dockerImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DockerImage createEntity() {
        DockerImage dockerImage = new DockerImage()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .version(DEFAULT_VERSION)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return dockerImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DockerImage createUpdatedEntity() {
        DockerImage dockerImage = new DockerImage()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return dockerImage;
    }

    @BeforeEach
    public void initTest() {
        dockerImageRepository.deleteAll();
        dockerImage = createEntity();
    }

    @Test
    void createDockerImage() throws Exception {
        int databaseSizeBeforeCreate = dockerImageRepository.findAll().size();
        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);
        restDockerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeCreate + 1);
        DockerImage testDockerImage = dockerImageList.get(dockerImageList.size() - 1);
        assertThat(testDockerImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDockerImage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDockerImage.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDockerImage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDockerImage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void createDockerImageWithExistingId() throws Exception {
        // Create the DockerImage with an existing ID
        dockerImage.setId("existing_id");
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        int databaseSizeBeforeCreate = dockerImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDockerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDockerImages() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        // Get all the dockerImageList
        restDockerImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dockerImage.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    void getDockerImage() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        // Get the dockerImage
        restDockerImageMockMvc
            .perform(get(ENTITY_API_URL_ID, dockerImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dockerImage.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    void getNonExistingDockerImage() throws Exception {
        // Get the dockerImage
        restDockerImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDockerImage() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();

        // Update the dockerImage
        DockerImage updatedDockerImage = dockerImageRepository.findById(dockerImage.getId()).get();
        updatedDockerImage
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(updatedDockerImage);

        restDockerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dockerImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
        DockerImage testDockerImage = dockerImageList.get(dockerImageList.size() - 1);
        assertThat(testDockerImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDockerImage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDockerImage.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDockerImage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDockerImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void putNonExistingDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dockerImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dockerImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDockerImageWithPatch() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();

        // Update the dockerImage using partial update
        DockerImage partialUpdatedDockerImage = new DockerImage();
        partialUpdatedDockerImage.setId(dockerImage.getId());

        partialUpdatedDockerImage.version(UPDATED_VERSION).createdBy(UPDATED_CREATED_BY);

        restDockerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDockerImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDockerImage))
            )
            .andExpect(status().isOk());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
        DockerImage testDockerImage = dockerImageList.get(dockerImageList.size() - 1);
        assertThat(testDockerImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDockerImage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDockerImage.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDockerImage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDockerImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void fullUpdateDockerImageWithPatch() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();

        // Update the dockerImage using partial update
        DockerImage partialUpdatedDockerImage = new DockerImage();
        partialUpdatedDockerImage.setId(dockerImage.getId());

        partialUpdatedDockerImage
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restDockerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDockerImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDockerImage))
            )
            .andExpect(status().isOk());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
        DockerImage testDockerImage = dockerImageList.get(dockerImageList.size() - 1);
        assertThat(testDockerImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDockerImage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDockerImage.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDockerImage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDockerImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void patchNonExistingDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dockerImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDockerImage() throws Exception {
        int databaseSizeBeforeUpdate = dockerImageRepository.findAll().size();
        dockerImage.setId(UUID.randomUUID().toString());

        // Create the DockerImage
        DockerImageDTO dockerImageDTO = dockerImageMapper.toDto(dockerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDockerImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dockerImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DockerImage in the database
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDockerImage() throws Exception {
        // Initialize the database
        dockerImageRepository.save(dockerImage);

        int databaseSizeBeforeDelete = dockerImageRepository.findAll().size();

        // Delete the dockerImage
        restDockerImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, dockerImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DockerImage> dockerImageList = dockerImageRepository.findAll();
        assertThat(dockerImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
