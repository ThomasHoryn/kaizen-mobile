package com.kaizenmobile.web.rest;

import com.kaizenmobile.domain.AppStats;
import com.kaizenmobile.repository.AppStatsRepository;
import com.kaizenmobile.service.AppStatsQueryService;
import com.kaizenmobile.service.AppStatsService;
import com.kaizenmobile.service.criteria.AppStatsCriteria;
import com.kaizenmobile.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kaizenmobile.domain.AppStats}.
 */
@RestController
@RequestMapping("/api/app-stats")
public class AppStatsResource {

    private static final Logger log = LoggerFactory.getLogger(AppStatsResource.class);

    private static final String ENTITY_NAME = "appStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppStatsService appStatsService;

    private final AppStatsRepository appStatsRepository;

    private final AppStatsQueryService appStatsQueryService;

    public AppStatsResource(
        AppStatsService appStatsService,
        AppStatsRepository appStatsRepository,
        AppStatsQueryService appStatsQueryService
    ) {
        this.appStatsService = appStatsService;
        this.appStatsRepository = appStatsRepository;
        this.appStatsQueryService = appStatsQueryService;
    }

    /**
     * {@code POST  /app-stats} : Create a new appStats.
     *
     * @param appStats the appStats to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appStats, or with status {@code 400 (Bad Request)} if the appStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppStats> createAppStats(@Valid @RequestBody AppStats appStats) throws URISyntaxException {
        log.debug("REST request to save AppStats : {}", appStats);
        if (appStats.getId() != null) {
            throw new BadRequestAlertException("A new appStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appStats = appStatsService.save(appStats);
        return ResponseEntity.created(new URI("/api/app-stats/" + appStats.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appStats.getId().toString()))
            .body(appStats);
    }

    /**
     * {@code PUT  /app-stats/:id} : Updates an existing appStats.
     *
     * @param id the id of the appStats to save.
     * @param appStats the appStats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appStats,
     * or with status {@code 400 (Bad Request)} if the appStats is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appStats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppStats> updateAppStats(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppStats appStats
    ) throws URISyntaxException {
        log.debug("REST request to update AppStats : {}, {}", id, appStats);
        if (appStats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appStats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appStats = appStatsService.update(appStats);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appStats.getId().toString()))
            .body(appStats);
    }

    /**
     * {@code PATCH  /app-stats/:id} : Partial updates given fields of an existing appStats, field will ignore if it is null
     *
     * @param id the id of the appStats to save.
     * @param appStats the appStats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appStats,
     * or with status {@code 400 (Bad Request)} if the appStats is not valid,
     * or with status {@code 404 (Not Found)} if the appStats is not found,
     * or with status {@code 500 (Internal Server Error)} if the appStats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppStats> partialUpdateAppStats(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppStats appStats
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppStats partially : {}, {}", id, appStats);
        if (appStats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appStats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppStats> result = appStatsService.partialUpdate(appStats);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appStats.getId().toString())
        );
    }

    /**
     * {@code GET  /app-stats} : get all the appStats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appStats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppStats>> getAllAppStats(AppStatsCriteria criteria) {
        log.debug("REST request to get AppStats by criteria: {}", criteria);

        List<AppStats> entityList = appStatsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /app-stats/count} : count all the appStats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAppStats(AppStatsCriteria criteria) {
        log.debug("REST request to count AppStats by criteria: {}", criteria);
        return ResponseEntity.ok().body(appStatsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-stats/:id} : get the "id" appStats.
     *
     * @param id the id of the appStats to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appStats, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppStats> getAppStats(@PathVariable("id") Long id) {
        log.debug("REST request to get AppStats : {}", id);
        Optional<AppStats> appStats = appStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appStats);
    }

    /**
     * {@code DELETE  /app-stats/:id} : delete the "id" appStats.
     *
     * @param id the id of the appStats to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppStats(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppStats : {}", id);
        appStatsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
