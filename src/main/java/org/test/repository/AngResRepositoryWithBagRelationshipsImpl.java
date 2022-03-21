package org.test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.test.domain.AngRes;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AngResRepositoryWithBagRelationshipsImpl implements AngResRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<AngRes> fetchBagRelationships(Optional<AngRes> angRes) {
        return angRes.map(this::fetchUids);
    }

    @Override
    public Page<AngRes> fetchBagRelationships(Page<AngRes> angRes) {
        return new PageImpl<>(fetchBagRelationships(angRes.getContent()), angRes.getPageable(), angRes.getTotalElements());
    }

    @Override
    public List<AngRes> fetchBagRelationships(List<AngRes> angRes) {
        return Optional.of(angRes).map(this::fetchUids).get();
    }

    AngRes fetchUids(AngRes result) {
        return entityManager
            .createQuery("select angRes from AngRes angRes left join fetch angRes.uids where angRes is :angRes", AngRes.class)
            .setParameter("angRes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AngRes> fetchUids(List<AngRes> angRes) {
        return entityManager
            .createQuery("select distinct angRes from AngRes angRes left join fetch angRes.uids where angRes in :angRes", AngRes.class)
            .setParameter("angRes", angRes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
