package org.test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.test.domain.AngRes;

public interface AngResRepositoryWithBagRelationships {
    Optional<AngRes> fetchBagRelationships(Optional<AngRes> angRes);

    List<AngRes> fetchBagRelationships(List<AngRes> angRes);

    Page<AngRes> fetchBagRelationships(Page<AngRes> angRes);
}
