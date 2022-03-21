package org.test.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.test.domain.AngGrp;

/**
 * Spring Data SQL repository for the AngGrp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AngGrpRepository extends JpaRepository<AngGrp, Long> {}
