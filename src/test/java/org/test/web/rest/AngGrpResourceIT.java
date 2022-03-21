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
import org.test.domain.AngGrp;
import org.test.repository.AngGrpRepository;

/**
 * Integration tests for the {@link AngGrpResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AngGrpResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_GRP_COD = "AAAAAAAAAA";
    private static final String UPDATED_GRP_COD = "BBBBBBBBBB";

    private static final String DEFAULT_GRP_DSC = "AAAAAAAAAA";
    private static final String UPDATED_GRP_DSC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ang-grps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AngGrpRepository angGrpRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAngGrpMockMvc;

    private AngGrp angGrp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngGrp createEntity(EntityManager em) {
        AngGrp angGrp = new AngGrp().uid(DEFAULT_UID).grpCod(DEFAULT_GRP_COD).grpDsc(DEFAULT_GRP_DSC);
        return angGrp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngGrp createUpdatedEntity(EntityManager em) {
        AngGrp angGrp = new AngGrp().uid(UPDATED_UID).grpCod(UPDATED_GRP_COD).grpDsc(UPDATED_GRP_DSC);
        return angGrp;
    }

    @BeforeEach
    public void initTest() {
        angGrp = createEntity(em);
    }

    @Test
    @Transactional
    void createAngGrp() throws Exception {
        int databaseSizeBeforeCreate = angGrpRepository.findAll().size();
        // Create the AngGrp
        restAngGrpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angGrp)))
            .andExpect(status().isCreated());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeCreate + 1);
        AngGrp testAngGrp = angGrpList.get(angGrpList.size() - 1);
        assertThat(testAngGrp.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAngGrp.getGrpCod()).isEqualTo(DEFAULT_GRP_COD);
        assertThat(testAngGrp.getGrpDsc()).isEqualTo(DEFAULT_GRP_DSC);
    }

    @Test
    @Transactional
    void createAngGrpWithExistingId() throws Exception {
        // Create the AngGrp with an existing ID
        angGrp.setId(1L);

        int databaseSizeBeforeCreate = angGrpRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAngGrpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angGrp)))
            .andExpect(status().isBadRequest());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAngGrps() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        // Get all the angGrpList
        restAngGrpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(angGrp.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].grpCod").value(hasItem(DEFAULT_GRP_COD)))
            .andExpect(jsonPath("$.[*].grpDsc").value(hasItem(DEFAULT_GRP_DSC)));
    }

    @Test
    @Transactional
    void getAngGrp() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        // Get the angGrp
        restAngGrpMockMvc
            .perform(get(ENTITY_API_URL_ID, angGrp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(angGrp.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.grpCod").value(DEFAULT_GRP_COD))
            .andExpect(jsonPath("$.grpDsc").value(DEFAULT_GRP_DSC));
    }

    @Test
    @Transactional
    void getNonExistingAngGrp() throws Exception {
        // Get the angGrp
        restAngGrpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAngGrp() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();

        // Update the angGrp
        AngGrp updatedAngGrp = angGrpRepository.findById(angGrp.getId()).get();
        // Disconnect from session so that the updates on updatedAngGrp are not directly saved in db
        em.detach(updatedAngGrp);
        updatedAngGrp.uid(UPDATED_UID).grpCod(UPDATED_GRP_COD).grpDsc(UPDATED_GRP_DSC);

        restAngGrpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAngGrp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAngGrp))
            )
            .andExpect(status().isOk());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
        AngGrp testAngGrp = angGrpList.get(angGrpList.size() - 1);
        assertThat(testAngGrp.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngGrp.getGrpCod()).isEqualTo(UPDATED_GRP_COD);
        assertThat(testAngGrp.getGrpDsc()).isEqualTo(UPDATED_GRP_DSC);
    }

    @Test
    @Transactional
    void putNonExistingAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, angGrp.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angGrp))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angGrp))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angGrp)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAngGrpWithPatch() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();

        // Update the angGrp using partial update
        AngGrp partialUpdatedAngGrp = new AngGrp();
        partialUpdatedAngGrp.setId(angGrp.getId());

        partialUpdatedAngGrp.uid(UPDATED_UID);

        restAngGrpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngGrp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngGrp))
            )
            .andExpect(status().isOk());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
        AngGrp testAngGrp = angGrpList.get(angGrpList.size() - 1);
        assertThat(testAngGrp.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngGrp.getGrpCod()).isEqualTo(DEFAULT_GRP_COD);
        assertThat(testAngGrp.getGrpDsc()).isEqualTo(DEFAULT_GRP_DSC);
    }

    @Test
    @Transactional
    void fullUpdateAngGrpWithPatch() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();

        // Update the angGrp using partial update
        AngGrp partialUpdatedAngGrp = new AngGrp();
        partialUpdatedAngGrp.setId(angGrp.getId());

        partialUpdatedAngGrp.uid(UPDATED_UID).grpCod(UPDATED_GRP_COD).grpDsc(UPDATED_GRP_DSC);

        restAngGrpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngGrp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngGrp))
            )
            .andExpect(status().isOk());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
        AngGrp testAngGrp = angGrpList.get(angGrpList.size() - 1);
        assertThat(testAngGrp.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngGrp.getGrpCod()).isEqualTo(UPDATED_GRP_COD);
        assertThat(testAngGrp.getGrpDsc()).isEqualTo(UPDATED_GRP_DSC);
    }

    @Test
    @Transactional
    void patchNonExistingAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, angGrp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angGrp))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angGrp))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAngGrp() throws Exception {
        int databaseSizeBeforeUpdate = angGrpRepository.findAll().size();
        angGrp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngGrpMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(angGrp)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngGrp in the database
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAngGrp() throws Exception {
        // Initialize the database
        angGrpRepository.saveAndFlush(angGrp);

        int databaseSizeBeforeDelete = angGrpRepository.findAll().size();

        // Delete the angGrp
        restAngGrpMockMvc
            .perform(delete(ENTITY_API_URL_ID, angGrp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AngGrp> angGrpList = angGrpRepository.findAll();
        assertThat(angGrpList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
