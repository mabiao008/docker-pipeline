package com.github.jadepeng.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.jadepeng.pipeline.IntegrationTest;
import com.github.jadepeng.pipeline.domain.ProgramVersion;
import com.github.jadepeng.pipeline.repository.ProgramVersionRepository;
import com.github.jadepeng.pipeline.service.dto.ProgramVersionDTO;
import com.github.jadepeng.pipeline.service.mapper.ProgramVersionMapper;
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
 * Integration tests for the {@link ProgramVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramVersionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_COMMAND = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/program-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProgramVersionRepository programVersionRepository;

    @Autowired
    private ProgramVersionMapper programVersionMapper;

    @Autowired
    private MockMvc restProgramVersionMockMvc;

    private ProgramVersion programVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramVersion createEntity() {
        ProgramVersion programVersion = new ProgramVersion()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .content(DEFAULT_CONTENT)
            .path(DEFAULT_PATH)
            .command(DEFAULT_COMMAND)
            .version(DEFAULT_VERSION)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return programVersion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramVersion createUpdatedEntity() {
        ProgramVersion programVersion = new ProgramVersion()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .path(UPDATED_PATH)
            .command(UPDATED_COMMAND)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return programVersion;
    }

    @BeforeEach
    public void initTest() {
        programVersionRepository.deleteAll();
        programVersion = createEntity();
    }

    @Test
    void createProgramVersion() throws Exception {
        int databaseSizeBeforeCreate = programVersionRepository.findAll().size();
        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);
        restProgramVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramVersion testProgramVersion = programVersionList.get(programVersionList.size() - 1);
        assertThat(testProgramVersion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProgramVersion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testProgramVersion.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testProgramVersion.getCommand()).isEqualTo(DEFAULT_COMMAND);
        assertThat(testProgramVersion.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testProgramVersion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgramVersion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void createProgramVersionWithExistingId() throws Exception {
        // Create the ProgramVersion with an existing ID
        programVersion.setId("existing_id");
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        int databaseSizeBeforeCreate = programVersionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramVersionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProgramVersions() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        // Get all the programVersionList
        restProgramVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programVersion.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].command").value(hasItem(DEFAULT_COMMAND)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    void getProgramVersion() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        // Get the programVersion
        restProgramVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, programVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programVersion.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.command").value(DEFAULT_COMMAND))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    void getNonExistingProgramVersion() throws Exception {
        // Get the programVersion
        restProgramVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProgramVersion() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();

        // Update the programVersion
        ProgramVersion updatedProgramVersion = programVersionRepository.findById(programVersion.getId()).get();
        updatedProgramVersion
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .path(UPDATED_PATH)
            .command(UPDATED_COMMAND)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(updatedProgramVersion);

        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
        ProgramVersion testProgramVersion = programVersionList.get(programVersionList.size() - 1);
        assertThat(testProgramVersion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgramVersion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProgramVersion.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testProgramVersion.getCommand()).isEqualTo(UPDATED_COMMAND);
        assertThat(testProgramVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProgramVersion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgramVersion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void putNonExistingProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgramVersionWithPatch() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();

        // Update the programVersion using partial update
        ProgramVersion partialUpdatedProgramVersion = new ProgramVersion();
        partialUpdatedProgramVersion.setId(programVersion.getId());

        partialUpdatedProgramVersion.name(UPDATED_NAME).content(UPDATED_CONTENT).version(UPDATED_VERSION).createdBy(UPDATED_CREATED_BY);

        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramVersion))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
        ProgramVersion testProgramVersion = programVersionList.get(programVersionList.size() - 1);
        assertThat(testProgramVersion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramVersion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProgramVersion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProgramVersion.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testProgramVersion.getCommand()).isEqualTo(DEFAULT_COMMAND);
        assertThat(testProgramVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProgramVersion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgramVersion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void fullUpdateProgramVersionWithPatch() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();

        // Update the programVersion using partial update
        ProgramVersion partialUpdatedProgramVersion = new ProgramVersion();
        partialUpdatedProgramVersion.setId(programVersion.getId());

        partialUpdatedProgramVersion
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .path(UPDATED_PATH)
            .command(UPDATED_COMMAND)
            .version(UPDATED_VERSION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramVersion))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
        ProgramVersion testProgramVersion = programVersionList.get(programVersionList.size() - 1);
        assertThat(testProgramVersion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramVersion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgramVersion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProgramVersion.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testProgramVersion.getCommand()).isEqualTo(UPDATED_COMMAND);
        assertThat(testProgramVersion.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProgramVersion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgramVersion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void patchNonExistingProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProgramVersion() throws Exception {
        int databaseSizeBeforeUpdate = programVersionRepository.findAll().size();
        programVersion.setId(UUID.randomUUID().toString());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programVersionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramVersion in the database
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProgramVersion() throws Exception {
        // Initialize the database
        programVersionRepository.save(programVersion);

        int databaseSizeBeforeDelete = programVersionRepository.findAll().size();

        // Delete the programVersion
        restProgramVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, programVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgramVersion> programVersionList = programVersionRepository.findAll();
        assertThat(programVersionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
