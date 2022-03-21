package org.test.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.test.domain.AngGrp;
import org.test.repository.AngGrpRepository;
import org.test.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.test.domain.AngGrp}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AngGrpResource {

    private final Logger log = LoggerFactory.getLogger(AngGrpResource.class);

    private static final String ENTITY_NAME = "angGrp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AngGrpRepository angGrpRepository;

    public AngGrpResource(AngGrpRepository angGrpRepository) {
        this.angGrpRepository = angGrpRepository;
    }

    /**
     * {@code POST  /ang-grps} : Create a new angGrp.
     *
     * @param angGrp the angGrp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new angGrp, or with status {@code 400 (Bad Request)} if the angGrp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ang-grps")
    public ResponseEntity<AngGrp> createAngGrp(@RequestBody AngGrp angGrp) throws URISyntaxException {
        log.debug("REST request to save AngGrp : {}", angGrp);
        if (angGrp.getId() != null) {
            throw new BadRequestAlertException("A new angGrp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AngGrp result = angGrpRepository.save(angGrp);
        return ResponseEntity
            .created(new URI("/api/ang-grps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ang-grps/:id} : Updates an existing angGrp.
     *
     * @param id the id of the angGrp to save.
     * @param angGrp the angGrp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angGrp,
     * or with status {@code 400 (Bad Request)} if the angGrp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the angGrp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ang-grps/{id}")
    public ResponseEntity<AngGrp> updateAngGrp(@PathVariable(value = "id", required = false) final Long id, @RequestBody AngGrp angGrp)
        throws URISyntaxException {
        log.debug("REST request to update AngGrp : {}, {}", id, angGrp);
        if (angGrp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angGrp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angGrpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AngGrp result = angGrpRepository.save(angGrp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angGrp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ang-grps/:id} : Partial updates given fields of an existing angGrp, field will ignore if it is null
     *
     * @param id the id of the angGrp to save.
     * @param angGrp the angGrp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angGrp,
     * or with status {@code 400 (Bad Request)} if the angGrp is not valid,
     * or with status {@code 404 (Not Found)} if the angGrp is not found,
     * or with status {@code 500 (Internal Server Error)} if the angGrp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ang-grps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AngGrp> partialUpdateAngGrp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AngGrp angGrp
    ) throws URISyntaxException {
        log.debug("REST request to partial update AngGrp partially : {}, {}", id, angGrp);
        if (angGrp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angGrp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angGrpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AngGrp> result = angGrpRepository
            .findById(angGrp.getId())
            .map(existingAngGrp -> {
                if (angGrp.getUid() != null) {
                    existingAngGrp.setUid(angGrp.getUid());
                }
                if (angGrp.getGrpCod() != null) {
                    existingAngGrp.setGrpCod(angGrp.getGrpCod());
                }
                if (angGrp.getGrpDsc() != null) {
                    existingAngGrp.setGrpDsc(angGrp.getGrpDsc());
                }

                return existingAngGrp;
            })
            .map(angGrpRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angGrp.getId().toString())
        );
    }

    /**
     * {@code GET  /ang-grps} : get all the angGrps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of angGrps in body.
     */
    @GetMapping("/ang-grps")
    public ResponseEntity<List<AngGrp>> getAllAngGrps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AngGrps");
        Page<AngGrp> page = angGrpRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ang-grps/:id} : get the "id" angGrp.
     *
     * @param id the id of the angGrp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the angGrp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ang-grps/{id}")
    public ResponseEntity<AngGrp> getAngGrp(@PathVariable Long id) {
        log.debug("REST request to get AngGrp : {}", id);
        Optional<AngGrp> angGrp = angGrpRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(angGrp);
    }

    /**
     * {@code DELETE  /ang-grps/:id} : delete the "id" angGrp.
     *
     * @param id the id of the angGrp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ang-grps/{id}")
    public ResponseEntity<Void> deleteAngGrp(@PathVariable Long id) {
        log.debug("REST request to delete AngGrp : {}", id);
        angGrpRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
