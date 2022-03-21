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
import org.test.domain.AngRes;
import org.test.repository.AngResRepository;
import org.test.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.test.domain.AngRes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AngResResource {

    private final Logger log = LoggerFactory.getLogger(AngResResource.class);

    private static final String ENTITY_NAME = "angRes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AngResRepository angResRepository;

    public AngResResource(AngResRepository angResRepository) {
        this.angResRepository = angResRepository;
    }

    /**
     * {@code POST  /ang-res} : Create a new angRes.
     *
     * @param angRes the angRes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new angRes, or with status {@code 400 (Bad Request)} if the angRes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ang-res")
    public ResponseEntity<AngRes> createAngRes(@RequestBody AngRes angRes) throws URISyntaxException {
        log.debug("REST request to save AngRes : {}", angRes);
        if (angRes.getId() != null) {
            throw new BadRequestAlertException("A new angRes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AngRes result = angResRepository.save(angRes);
        return ResponseEntity
            .created(new URI("/api/ang-res/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ang-res/:id} : Updates an existing angRes.
     *
     * @param id the id of the angRes to save.
     * @param angRes the angRes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angRes,
     * or with status {@code 400 (Bad Request)} if the angRes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the angRes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ang-res/{id}")
    public ResponseEntity<AngRes> updateAngRes(@PathVariable(value = "id", required = false) final Long id, @RequestBody AngRes angRes)
        throws URISyntaxException {
        log.debug("REST request to update AngRes : {}, {}", id, angRes);
        if (angRes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angRes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angResRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AngRes result = angResRepository.save(angRes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angRes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ang-res/:id} : Partial updates given fields of an existing angRes, field will ignore if it is null
     *
     * @param id the id of the angRes to save.
     * @param angRes the angRes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angRes,
     * or with status {@code 400 (Bad Request)} if the angRes is not valid,
     * or with status {@code 404 (Not Found)} if the angRes is not found,
     * or with status {@code 500 (Internal Server Error)} if the angRes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ang-res/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AngRes> partialUpdateAngRes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AngRes angRes
    ) throws URISyntaxException {
        log.debug("REST request to partial update AngRes partially : {}, {}", id, angRes);
        if (angRes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angRes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angResRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AngRes> result = angResRepository
            .findById(angRes.getId())
            .map(existingAngRes -> {
                if (angRes.getUid() != null) {
                    existingAngRes.setUid(angRes.getUid());
                }
                if (angRes.getResCod() != null) {
                    existingAngRes.setResCod(angRes.getResCod());
                }
                if (angRes.getResDsc() != null) {
                    existingAngRes.setResDsc(angRes.getResDsc());
                }
                if (angRes.getBdgUid() != null) {
                    existingAngRes.setBdgUid(angRes.getBdgUid());
                }
                if (angRes.getResTyp() != null) {
                    existingAngRes.setResTyp(angRes.getResTyp());
                }

                return existingAngRes;
            })
            .map(angResRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angRes.getId().toString())
        );
    }

    /**
     * {@code GET  /ang-res} : get all the angRes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of angRes in body.
     */
    @GetMapping("/ang-res")
    public ResponseEntity<List<AngRes>> getAllAngRes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of AngRes");
        Page<AngRes> page;
        if (eagerload) {
            page = angResRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = angResRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ang-res/:id} : get the "id" angRes.
     *
     * @param id the id of the angRes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the angRes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ang-res/{id}")
    public ResponseEntity<AngRes> getAngRes(@PathVariable Long id) {
        log.debug("REST request to get AngRes : {}", id);
        Optional<AngRes> angRes = angResRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(angRes);
    }

    /**
     * {@code DELETE  /ang-res/:id} : delete the "id" angRes.
     *
     * @param id the id of the angRes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ang-res/{id}")
    public ResponseEntity<Void> deleteAngRes(@PathVariable Long id) {
        log.debug("REST request to delete AngRes : {}", id);
        angResRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
