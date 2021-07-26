package com.github.jadepeng.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.jadepeng.pipeline.IntegrationTest;
import com.github.jadepeng.pipeline.domain.App;
import com.github.jadepeng.pipeline.repository.AppRepository;
import com.github.jadepeng.pipeline.service.dto.AppDTO;
import com.github.jadepeng.pipeline.service.mapper.AppMapper;
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
 * Integration tests for the {@link AppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private MockMvc restAppMockMvc;

    private App app;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createEntity() {
        App app = new App().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE).createdBy(DEFAULT_CREATED_BY);
        return app;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createUpdatedEntity() {
        App app = new App().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).createdBy(UPDATED_CREATED_BY);
        return app;
    }

    @BeforeEach
    public void initTest() {
        appRepository.deleteAll();
        app = createEntity();
    }

    @Test
    void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();
        // Create the App
        AppDTO appDTO = appMapper.toDto(app);
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApp.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testApp.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void createAppWithExistingId() throws Exception {
        // Create the App with an existing ID
        app.setId("existing_id");
        AppDTO appDTO = appMapper.toDto(app);

        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllApps() throws Exception {
        // Initialize the database
        appRepository.save(app);

        // Get all the appList
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    void getApp() throws Exception {
        // Initialize the database
        appRepository.save(app);

        // Get the app
        restAppMockMvc
            .perform(get(ENTITY_API_URL_ID, app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewApp() throws Exception {
        // Initialize the database
        appRepository.save(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).get();
        updatedApp.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).createdBy(UPDATED_CREATED_BY);
        AppDTO appDTO = appMapper.toDto(updatedApp);

        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApp.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testApp.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void putNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.save(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApp.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testApp.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    void fullUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.save(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).createdBy(UPDATED_CREATED_BY);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApp.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testApp.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    void patchNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(UUID.randomUUID().toString());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApp() throws Exception {
        // Initialize the database
        appRepository.save(app);

        int databaseSizeBeforeDelete = appRepository.findAll().size();

        // Delete the app
        restAppMockMvc.perform(delete(ENTITY_API_URL_ID, app.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
