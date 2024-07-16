package com.kaizenmobile.service;

import com.kaizenmobile.domain.AppStats;
import com.kaizenmobile.repository.AppStatsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kaizenmobile.domain.AppStats}.
 */
@Service
@Transactional
public class AppStatsService {

    private static final Logger log = LoggerFactory.getLogger(AppStatsService.class);

    private final AppStatsRepository appStatsRepository;

    public AppStatsService(AppStatsRepository appStatsRepository) {
        this.appStatsRepository = appStatsRepository;
    }

    /**
     * Save a appStats.
     *
     * @param appStats the entity to save.
     * @return the persisted entity.
     */
    public AppStats save(AppStats appStats) {
        log.debug("Request to save AppStats : {}", appStats);
        return appStatsRepository.save(appStats);
    }

    /**
     * Update a appStats.
     *
     * @param appStats the entity to save.
     * @return the persisted entity.
     */
    public AppStats update(AppStats appStats) {
        log.debug("Request to update AppStats : {}", appStats);
        return appStatsRepository.save(appStats);
    }

    /**
     * Partially update a appStats.
     *
     * @param appStats the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppStats> partialUpdate(AppStats appStats) {
        log.debug("Request to partially update AppStats : {}", appStats);

        return appStatsRepository
            .findById(appStats.getId())
            .map(existingAppStats -> {
                if (appStats.getUsedTenantId() != null) {
                    existingAppStats.setUsedTenantId(appStats.getUsedTenantId());
                }

                return existingAppStats;
            })
            .map(appStatsRepository::save);
    }

    /**
     * Get one appStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppStats> findOne(Long id) {
        log.debug("Request to get AppStats : {}", id);
        return appStatsRepository.findById(id);
    }

    /**
     * Delete the appStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppStats : {}", id);
        appStatsRepository.deleteById(id);
    }
}
