package org.test.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import org.test.domain.AngBdg;
import org.test.repository.AngBdgRepository;
import org.test.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.test.domain.AngBdg}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AngBdgResource {

    private final Logger log = LoggerFactory.getLogger(AngBdgResource.class);

    private static final String ENTITY_NAME = "angBdg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AngBdgRepository angBdgRepository;

    public AngBdgResource(AngBdgRepository angBdgRepository) {
        this.angBdgRepository = angBdgRepository;
    }

    /**
     * {@code POST  /ang-bdgs} : Create a new angBdg.
     *
     * @param angBdg the angBdg to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new angBdg, or with status {@code 400 (Bad Request)} if the angBdg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ang-bdgs")
    public ResponseEntity<AngBdg> createAngBdg(@Valid @RequestBody AngBdg angBdg) throws URISyntaxException {
        log.debug("REST request to save AngBdg : {}", angBdg);
        if (angBdg.getId() != null) {
            throw new BadRequestAlertException("A new angBdg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AngBdg result = angBdgRepository.save(angBdg);
        return ResponseEntity
            .created(new URI("/api/ang-bdgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ang-bdgs/:id} : Updates an existing angBdg.
     *
     * @param id the id of the angBdg to save.
     * @param angBdg the angBdg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angBdg,
     * or with status {@code 400 (Bad Request)} if the angBdg is not valid,
     * or with status {@code 500 (Internal Server Error)} if the angBdg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ang-bdgs/{id}")
    public ResponseEntity<AngBdg> updateAngBdg(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AngBdg angBdg
    ) throws URISyntaxException {
        log.debug("REST request to update AngBdg : {}, {}", id, angBdg);
        if (angBdg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angBdg.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angBdgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AngBdg result = angBdgRepository.save(angBdg);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angBdg.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ang-bdgs/:id} : Partial updates given fields of an existing angBdg, field will ignore if it is null
     *
     * @param id the id of the angBdg to save.
     * @param angBdg the angBdg to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated angBdg,
     * or with status {@code 400 (Bad Request)} if the angBdg is not valid,
     * or with status {@code 404 (Not Found)} if the angBdg is not found,
     * or with status {@code 500 (Internal Server Error)} if the angBdg couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ang-bdgs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AngBdg> partialUpdateAngBdg(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AngBdg angBdg
    ) throws URISyntaxException {
        log.debug("REST request to partial update AngBdg partially : {}, {}", id, angBdg);
        if (angBdg.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, angBdg.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!angBdgRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AngBdg> result = angBdgRepository
            .findById(angBdg.getId())
            .map(existingAngBdg -> {
                if (angBdg.getUid() != null) {
                    existingAngBdg.setUid(angBdg.getUid());
                }
                if (angBdg.getBdgCod() != null) {
                    existingAngBdg.setBdgCod(angBdg.getBdgCod());
                }

                return existingAngBdg;
            })
            .map(angBdgRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, angBdg.getId().toString())
        );
    }

    /**
     * {@code GET  /ang-bdgs} : get all the angBdgs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of angBdgs in body.
     */
    @GetMapping("/ang-bdgs")
    public ResponseEntity<List<AngBdg>> getAllAngBdgs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AngBdgs");
        Page<AngBdg> page = angBdgRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ang-bdgs/:id} : get the "id" angBdg.
     *
     * @param id the id of the angBdg to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the angBdg, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ang-bdgs/{id}")
    public ResponseEntity<AngBdg> getAngBdg(@PathVariable Long id) {
        log.debug("REST request to get AngBdg : {}", id);
        Optional<AngBdg> angBdg = angBdgRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(angBdg);
    }

    /**
     * {@code DELETE  /ang-bdgs/:id} : delete the "id" angBdg.
     *
     * @param id the id of the angBdg to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ang-bdgs/{id}")
    public ResponseEntity<Void> deleteAngBdg(@PathVariable Long id) {
        log.debug("REST request to delete AngBdg : {}", id);
        angBdgRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
