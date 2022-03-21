package org.test.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.test.domain.AngBdg;

/**
 * Spring Data SQL repository for the AngBdg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AngBdgRepository extends JpaRepository<AngBdg, Long> {}
