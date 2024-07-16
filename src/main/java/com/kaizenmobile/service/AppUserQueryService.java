package com.kaizenmobile.service;

import com.kaizenmobile.domain.*; // for static metamodels
import com.kaizenmobile.domain.AppUser;
import com.kaizenmobile.repository.AppUserRepository;
import com.kaizenmobile.service.criteria.AppUserCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppUser} entities in the database.
 * The main input is a {@link AppUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppUserQueryService extends QueryService<AppUser> {

    private static final Logger log = LoggerFactory.getLogger(AppUserQueryService.class);

    private final AppUserRepository appUserRepository;

    public AppUserQueryService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Return a {@link List} of {@link AppUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findByCriteria(AppUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserRepository.count(specification);
    }

    /**
     * Function to convert {@link AppUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppUser> createSpecification(AppUserCriteria criteria) {
        Specification<AppUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppUser_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantId(), AppUser_.tenantId));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(AppUser_.internalUser, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
