package com.kaizenmobile.web.rest;

import static com.kaizenmobile.domain.AppStatsAsserts.*;
import static com.kaizenmobile.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaizenmobile.IntegrationTest;
import com.kaizenmobile.domain.AppStats;
import com.kaizenmobile.repository.AppStatsRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppStatsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppStatsResourceIT {

    private static final String DEFAULT_USED_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_USED_TENANT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppStatsRepository appStatsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppStatsMockMvc;

    private AppStats appStats;

    private AppStats insertedAppStats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppStats createEntity(EntityManager em) {
        AppStats appStats = new AppStats().usedTenantId(DEFAULT_USED_TENANT_ID);
        return appStats;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppStats createUpdatedEntity(EntityManager em) {
        AppStats appStats = new AppStats().usedTenantId(UPDATED_USED_TENANT_ID);
        return appStats;
    }

    @BeforeEach
    public void initTest() {
        appStats = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppStats != null) {
            appStatsRepository.delete(insertedAppStats);
            insertedAppStats = null;
        }
    }

    @Test
    @Transactional
    void createAppStats() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppStats
        var returnedAppStats = om.readValue(
            restAppStatsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appStats)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppStats.class
        );

        // Validate the AppStats in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppStatsUpdatableFieldsEquals(returnedAppStats, getPersistedAppStats(returnedAppStats));

        insertedAppStats = returnedAppStats;
    }

    @Test
    @Transactional
    void createAppStatsWithExistingId() throws Exception {
        // Create the AppStats with an existing ID
        appStats.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppStatsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appStats)))
            .andExpect(status().isBadRequest());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppStats() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].usedTenantId").value(hasItem(DEFAULT_USED_TENANT_ID)));
    }

    @Test
    @Transactional
    void getAppStats() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get the appStats
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL_ID, appStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appStats.getId().intValue()))
            .andExpect(jsonPath("$.usedTenantId").value(DEFAULT_USED_TENANT_ID));
    }

    @Test
    @Transactional
    void getAppStatsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        Long id = appStats.getId();

        defaultAppStatsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppStatsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppStatsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppStatsByUsedTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList where usedTenantId equals to
        defaultAppStatsFiltering("usedTenantId.equals=" + DEFAULT_USED_TENANT_ID, "usedTenantId.equals=" + UPDATED_USED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllAppStatsByUsedTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList where usedTenantId in
        defaultAppStatsFiltering(
            "usedTenantId.in=" + DEFAULT_USED_TENANT_ID + "," + UPDATED_USED_TENANT_ID,
            "usedTenantId.in=" + UPDATED_USED_TENANT_ID
        );
    }

    @Test
    @Transactional
    void getAllAppStatsByUsedTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList where usedTenantId is not null
        defaultAppStatsFiltering("usedTenantId.specified=true", "usedTenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllAppStatsByUsedTenantIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList where usedTenantId contains
        defaultAppStatsFiltering("usedTenantId.contains=" + DEFAULT_USED_TENANT_ID, "usedTenantId.contains=" + UPDATED_USED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllAppStatsByUsedTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        // Get all the appStatsList where usedTenantId does not contain
        defaultAppStatsFiltering(
            "usedTenantId.doesNotContain=" + UPDATED_USED_TENANT_ID,
            "usedTenantId.doesNotContain=" + DEFAULT_USED_TENANT_ID
        );
    }

    private void defaultAppStatsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppStatsShouldBeFound(shouldBeFound);
        defaultAppStatsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppStatsShouldBeFound(String filter) throws Exception {
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].usedTenantId").value(hasItem(DEFAULT_USED_TENANT_ID)));

        // Check, that the count call also returns 1
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppStatsShouldNotBeFound(String filter) throws Exception {
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppStatsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppStats() throws Exception {
        // Get the appStats
        restAppStatsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppStats() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appStats
        AppStats updatedAppStats = appStatsRepository.findById(appStats.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppStats are not directly saved in db
        em.detach(updatedAppStats);
        updatedAppStats.usedTenantId(UPDATED_USED_TENANT_ID);

        restAppStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppStats.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppStats))
            )
            .andExpect(status().isOk());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppStatsToMatchAllProperties(updatedAppStats);
    }

    @Test
    @Transactional
    void putNonExistingAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appStats.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appStats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppStatsWithPatch() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appStats using partial update
        AppStats partialUpdatedAppStats = new AppStats();
        partialUpdatedAppStats.setId(appStats.getId());

        partialUpdatedAppStats.usedTenantId(UPDATED_USED_TENANT_ID);

        restAppStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppStats))
            )
            .andExpect(status().isOk());

        // Validate the AppStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppStatsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppStats, appStats), getPersistedAppStats(appStats));
    }

    @Test
    @Transactional
    void fullUpdateAppStatsWithPatch() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appStats using partial update
        AppStats partialUpdatedAppStats = new AppStats();
        partialUpdatedAppStats.setId(appStats.getId());

        partialUpdatedAppStats.usedTenantId(UPDATED_USED_TENANT_ID);

        restAppStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppStats))
            )
            .andExpect(status().isOk());

        // Validate the AppStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppStatsUpdatableFieldsEquals(partialUpdatedAppStats, getPersistedAppStats(partialUpdatedAppStats));
    }

    @Test
    @Transactional
    void patchNonExistingAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppStatsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appStats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppStats() throws Exception {
        // Initialize the database
        insertedAppStats = appStatsRepository.saveAndFlush(appStats);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appStats
        restAppStatsMockMvc
            .perform(delete(ENTITY_API_URL_ID, appStats.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appStatsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AppStats getPersistedAppStats(AppStats appStats) {
        return appStatsRepository.findById(appStats.getId()).orElseThrow();
    }

    protected void assertPersistedAppStatsToMatchAllProperties(AppStats expectedAppStats) {
        assertAppStatsAllPropertiesEquals(expectedAppStats, getPersistedAppStats(expectedAppStats));
    }

    protected void assertPersistedAppStatsToMatchUpdatableProperties(AppStats expectedAppStats) {
        assertAppStatsAllUpdatablePropertiesEquals(expectedAppStats, getPersistedAppStats(expectedAppStats));
    }
}
