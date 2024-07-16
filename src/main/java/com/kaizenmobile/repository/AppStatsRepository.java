package com.kaizenmobile.repository;

import com.kaizenmobile.domain.AppStats;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppStatsRepository extends JpaRepository<AppStats, Long>, JpaSpecificationExecutor<AppStats> {}
