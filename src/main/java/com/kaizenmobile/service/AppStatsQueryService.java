package com.kaizenmobile.service;

import com.kaizenmobile.domain.*; // for static metamodels
import com.kaizenmobile.domain.AppStats;
import com.kaizenmobile.repository.AppStatsRepository;
import com.kaizenmobile.service.criteria.AppStatsCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppStats} entities in the database.
 * The main input is a {@link AppStatsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppStats} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppStatsQueryService extends QueryService<AppStats> {

    private static final Logger log = LoggerFactory.getLogger(AppStatsQueryService.class);

    private final AppStatsRepository appStatsRepository;

    public AppStatsQueryService(AppStatsRepository appStatsRepository) {
        this.appStatsRepository = appStatsRepository;
    }

    /**
     * Return a {@link List} of {@link AppStats} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppStats> findByCriteria(AppStatsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppStats> specification = createSpecification(criteria);
        return appStatsRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppStatsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppStats> specification = createSpecification(criteria);
        return appStatsRepository.count(specification);
    }

    /**
     * Function to convert {@link AppStatsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppStats> createSpecification(AppStatsCriteria criteria) {
        Specification<AppStats> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppStats_.id));
            }
            if (criteria.getUsedTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsedTenantId(), AppStats_.usedTenantId));
            }
        }
        return specification;
    }
}
