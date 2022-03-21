package org.test.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.test.IntegrationTest;
import org.test.domain.AngBdg;
import org.test.repository.AngBdgRepository;

/**
 * Integration tests for the {@link AngBdgResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AngBdgResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_BDG_COD = "AAAAAAAAAA";
    private static final String UPDATED_BDG_COD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ang-bdgs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AngBdgRepository angBdgRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAngBdgMockMvc;

    private AngBdg angBdg;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngBdg createEntity(EntityManager em) {
        AngBdg angBdg = new AngBdg().uid(DEFAULT_UID).bdgCod(DEFAULT_BDG_COD);
        return angBdg;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngBdg createUpdatedEntity(EntityManager em) {
        AngBdg angBdg = new AngBdg().uid(UPDATED_UID).bdgCod(UPDATED_BDG_COD);
        return angBdg;
    }

    @BeforeEach
    public void initTest() {
        angBdg = createEntity(em);
    }

    @Test
    @Transactional
    void createAngBdg() throws Exception {
        int databaseSizeBeforeCreate = angBdgRepository.findAll().size();
        // Create the AngBdg
        restAngBdgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angBdg)))
            .andExpect(status().isCreated());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeCreate + 1);
        AngBdg testAngBdg = angBdgList.get(angBdgList.size() - 1);
        assertThat(testAngBdg.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAngBdg.getBdgCod()).isEqualTo(DEFAULT_BDG_COD);
    }

    @Test
    @Transactional
    void createAngBdgWithExistingId() throws Exception {
        // Create the AngBdg with an existing ID
        angBdg.setId(1L);

        int databaseSizeBeforeCreate = angBdgRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAngBdgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angBdg)))
            .andExpect(status().isBadRequest());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBdgCodIsRequired() throws Exception {
        int databaseSizeBeforeTest = angBdgRepository.findAll().size();
        // set the field null
        angBdg.setBdgCod(null);

        // Create the AngBdg, which fails.

        restAngBdgMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angBdg)))
            .andExpect(status().isBadRequest());

        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAngBdgs() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        // Get all the angBdgList
        restAngBdgMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(angBdg.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].bdgCod").value(hasItem(DEFAULT_BDG_COD)));
    }

    @Test
    @Transactional
    void getAngBdg() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        // Get the angBdg
        restAngBdgMockMvc
            .perform(get(ENTITY_API_URL_ID, angBdg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(angBdg.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.bdgCod").value(DEFAULT_BDG_COD));
    }

    @Test
    @Transactional
    void getNonExistingAngBdg() throws Exception {
        // Get the angBdg
        restAngBdgMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAngBdg() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();

        // Update the angBdg
        AngBdg updatedAngBdg = angBdgRepository.findById(angBdg.getId()).get();
        // Disconnect from session so that the updates on updatedAngBdg are not directly saved in db
        em.detach(updatedAngBdg);
        updatedAngBdg.uid(UPDATED_UID).bdgCod(UPDATED_BDG_COD);

        restAngBdgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAngBdg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAngBdg))
            )
            .andExpect(status().isOk());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
        AngBdg testAngBdg = angBdgList.get(angBdgList.size() - 1);
        assertThat(testAngBdg.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngBdg.getBdgCod()).isEqualTo(UPDATED_BDG_COD);
    }

    @Test
    @Transactional
    void putNonExistingAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, angBdg.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angBdg))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angBdg))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angBdg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAngBdgWithPatch() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();

        // Update the angBdg using partial update
        AngBdg partialUpdatedAngBdg = new AngBdg();
        partialUpdatedAngBdg.setId(angBdg.getId());

        partialUpdatedAngBdg.bdgCod(UPDATED_BDG_COD);

        restAngBdgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngBdg.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngBdg))
            )
            .andExpect(status().isOk());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
        AngBdg testAngBdg = angBdgList.get(angBdgList.size() - 1);
        assertThat(testAngBdg.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAngBdg.getBdgCod()).isEqualTo(UPDATED_BDG_COD);
    }

    @Test
    @Transactional
    void fullUpdateAngBdgWithPatch() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();

        // Update the angBdg using partial update
        AngBdg partialUpdatedAngBdg = new AngBdg();
        partialUpdatedAngBdg.setId(angBdg.getId());

        partialUpdatedAngBdg.uid(UPDATED_UID).bdgCod(UPDATED_BDG_COD);

        restAngBdgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngBdg.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngBdg))
            )
            .andExpect(status().isOk());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
        AngBdg testAngBdg = angBdgList.get(angBdgList.size() - 1);
        assertThat(testAngBdg.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngBdg.getBdgCod()).isEqualTo(UPDATED_BDG_COD);
    }

    @Test
    @Transactional
    void patchNonExistingAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, angBdg.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angBdg))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angBdg))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAngBdg() throws Exception {
        int databaseSizeBeforeUpdate = angBdgRepository.findAll().size();
        angBdg.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngBdgMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(angBdg)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngBdg in the database
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAngBdg() throws Exception {
        // Initialize the database
        angBdgRepository.saveAndFlush(angBdg);

        int databaseSizeBeforeDelete = angBdgRepository.findAll().size();

        // Delete the angBdg
        restAngBdgMockMvc
            .perform(delete(ENTITY_API_URL_ID, angBdg.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AngBdg> angBdgList = angBdgRepository.findAll();
        assertThat(angBdgList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
