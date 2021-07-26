package com.github.jadepeng.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.jadepeng.pipeline.IntegrationTest;
import com.github.jadepeng.pipeline.domain.ProgramArguments;
import com.github.jadepeng.pipeline.repository.ProgramArgumentsRepository;
import com.github.jadepeng.pipeline.service.dto.ProgramArgumentsDTO;
import com.github.jadepeng.pipeline.service.mapper.ProgramArgumentsMapper;
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
 * Integration tests for the {@link ProgramArgumentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramArgumentsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FORM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_PLACEHOLDER = "AAAAAAAAAA";
    private static final String UPDATED_PLACEHOLDER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/program-arguments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProgramArgumentsRepository programArgumentsRepository;

    @Autowired
    private ProgramArgumentsMapper programArgumentsMapper;

    @Autowired
    private MockMvc restProgramArgumentsMockMvc;

    private ProgramArguments programArguments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramArguments createEntity() {
        ProgramArguments programArguments = new ProgramArguments()
            .name(DEFAULT_NAME)
            .tooltip(DEFAULT_TOOLTIP)
            .formType(DEFAULT_FORM_TYPE)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .placeholder(DEFAULT_PLACEHOLDER)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return programArguments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramArguments createUpdatedEntity() {
        ProgramArguments programArguments = new ProgramArguments()
            .name(UPDATED_NAME)
            .tooltip(UPDATED_TOOLTIP)
            .formType(UPDATED_FORM_TYPE)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .placeholder(UPDATED_PLACEHOLDER)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return programArguments;
    }

    @BeforeEach
    public void initTest() {
        programArgumentsRepository.deleteAll();
        programArguments = createEntity();
    }

    @Test
    void createProgramArguments() throws Exception {
        int databaseSizeBeforeCreate = programArgumentsRepository.findAll().size();
        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);
        restProgramArgumentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramArguments testProgramArguments = programArgumentsList.get(programArgumentsList.size() - 1);
        assertThat(testProgramArguments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramArguments.getTooltip()).isEqualTo(DEFAULT_TOOLTIP);
        assertThat(testProgramArguments.getFormType()).isEqualTo(DEFAULT_FORM_TYPE);
        assertThat(testProgramArguments.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testProgramArguments.getPlaceholder()).isEqualTo(DEFAULT_PLACEHOLDER);
        assertThat(testProgramArguments.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgramArguments.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void createProgramArgumentsWithExistingId() throws Exception {
        // Create the ProgramArguments with an existing ID
        programArguments.setId("existing_id");
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        int databaseSizeBeforeCreate = programArgumentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramArgumentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProgramArguments() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        // Get all the programArgumentsList
        restProgramArgumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programArguments.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tooltip").value(hasItem(DEFAULT_TOOLTIP)))
            .andExpect(jsonPath("$.[*].formType").value(hasItem(DEFAULT_FORM_TYPE)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].placeholder").value(hasItem(DEFAULT_PLACEHOLDER)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    void getProgramArguments() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        // Get the programArguments
        restProgramArgumentsMockMvc
            .perform(get(ENTITY_API_URL_ID, programArguments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programArguments.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.tooltip").value(DEFAULT_TOOLTIP))
            .andExpect(jsonPath("$.formType").value(DEFAULT_FORM_TYPE))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.placeholder").value(DEFAULT_PLACEHOLDER))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    void getNonExistingProgramArguments() throws Exception {
        // Get the programArguments
        restProgramArgumentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProgramArguments() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();

        // Update the programArguments
        ProgramArguments updatedProgramArguments = programArgumentsRepository.findById(programArguments.getId()).get();
        updatedProgramArguments
            .name(UPDATED_NAME)
            .tooltip(UPDATED_TOOLTIP)
            .formType(UPDATED_FORM_TYPE)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .placeholder(UPDATED_PLACEHOLDER)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(updatedProgramArguments);

        restProgramArgumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programArgumentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
        ProgramArguments testProgramArguments = programArgumentsList.get(programArgumentsList.size() - 1);
        assertThat(testProgramArguments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramArguments.getTooltip()).isEqualTo(UPDATED_TOOLTIP);
        assertThat(testProgramArguments.getFormType()).isEqualTo(UPDATED_FORM_TYPE);
        assertThat(testProgramArguments.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testProgramArguments.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
        assertThat(testProgramArguments.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgramArguments.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void putNonExistingProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programArgumentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgramArgumentsWithPatch() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();

        // Update the programArguments using partial update
        ProgramArguments partialUpdatedProgramArguments = new ProgramArguments();
        partialUpdatedProgramArguments.setId(programArguments.getId());

        partialUpdatedProgramArguments.name(UPDATED_NAME).placeholder(UPDATED_PLACEHOLDER);

        restProgramArgumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramArguments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramArguments))
            )
            .andExpect(status().isOk());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
        ProgramArguments testProgramArguments = programArgumentsList.get(programArgumentsList.size() - 1);
        assertThat(testProgramArguments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramArguments.getTooltip()).isEqualTo(DEFAULT_TOOLTIP);
        assertThat(testProgramArguments.getFormType()).isEqualTo(DEFAULT_FORM_TYPE);
        assertThat(testProgramArguments.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testProgramArguments.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
        assertThat(testProgramArguments.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgramArguments.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void fullUpdateProgramArgumentsWithPatch() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();

        // Update the programArguments using partial update
        ProgramArguments partialUpdatedProgramArguments = new ProgramArguments();
        partialUpdatedProgramArguments.setId(programArguments.getId());

        partialUpdatedProgramArguments
            .name(UPDATED_NAME)
            .tooltip(UPDATED_TOOLTIP)
            .formType(UPDATED_FORM_TYPE)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .placeholder(UPDATED_PLACEHOLDER)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restProgramArgumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramArguments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramArguments))
            )
            .andExpect(status().isOk());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
        ProgramArguments testProgramArguments = programArgumentsList.get(programArgumentsList.size() - 1);
        assertThat(testProgramArguments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramArguments.getTooltip()).isEqualTo(UPDATED_TOOLTIP);
        assertThat(testProgramArguments.getFormType()).isEqualTo(UPDATED_FORM_TYPE);
        assertThat(testProgramArguments.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testProgramArguments.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
        assertThat(testProgramArguments.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgramArguments.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void patchNonExistingProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programArgumentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProgramArguments() throws Exception {
        int databaseSizeBeforeUpdate = programArgumentsRepository.findAll().size();
        programArguments.setId(UUID.randomUUID().toString());

        // Create the ProgramArguments
        ProgramArgumentsDTO programArgumentsDTO = programArgumentsMapper.toDto(programArguments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramArgumentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programArgumentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramArguments in the database
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProgramArguments() throws Exception {
        // Initialize the database
        programArgumentsRepository.save(programArguments);

        int databaseSizeBeforeDelete = programArgumentsRepository.findAll().size();

        // Delete the programArguments
        restProgramArgumentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, programArguments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgramArguments> programArgumentsList = programArgumentsRepository.findAll();
        assertThat(programArgumentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
