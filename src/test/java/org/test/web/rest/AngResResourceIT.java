package org.test.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.test.IntegrationTest;
import org.test.domain.AngRes;
import org.test.domain.enumeration.AngResTyp;
import org.test.repository.AngResRepository;

/**
 * Integration tests for the {@link AngResResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AngResResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_RES_COD = "AAAAAAAAAA";
    private static final String UPDATED_RES_COD = "BBBBBBBBBB";

    private static final String DEFAULT_RES_DSC = "AAAAAAAAAA";
    private static final String UPDATED_RES_DSC = "BBBBBBBBBB";

    private static final Long DEFAULT_BDG_UID = 1L;
    private static final Long UPDATED_BDG_UID = 2L;

    private static final AngResTyp DEFAULT_RES_TYP = AngResTyp.H;
    private static final AngResTyp UPDATED_RES_TYP = AngResTyp.M;

    private static final String ENTITY_API_URL = "/api/ang-res";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AngResRepository angResRepository;

    @Mock
    private AngResRepository angResRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAngResMockMvc;

    private AngRes angRes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngRes createEntity(EntityManager em) {
        AngRes angRes = new AngRes()
            .uid(DEFAULT_UID)
            .resCod(DEFAULT_RES_COD)
            .resDsc(DEFAULT_RES_DSC)
            .bdgUid(DEFAULT_BDG_UID)
            .resTyp(DEFAULT_RES_TYP);
        return angRes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AngRes createUpdatedEntity(EntityManager em) {
        AngRes angRes = new AngRes()
            .uid(UPDATED_UID)
            .resCod(UPDATED_RES_COD)
            .resDsc(UPDATED_RES_DSC)
            .bdgUid(UPDATED_BDG_UID)
            .resTyp(UPDATED_RES_TYP);
        return angRes;
    }

    @BeforeEach
    public void initTest() {
        angRes = createEntity(em);
    }

    @Test
    @Transactional
    void createAngRes() throws Exception {
        int databaseSizeBeforeCreate = angResRepository.findAll().size();
        // Create the AngRes
        restAngResMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angRes)))
            .andExpect(status().isCreated());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeCreate + 1);
        AngRes testAngRes = angResList.get(angResList.size() - 1);
        assertThat(testAngRes.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAngRes.getResCod()).isEqualTo(DEFAULT_RES_COD);
        assertThat(testAngRes.getResDsc()).isEqualTo(DEFAULT_RES_DSC);
        assertThat(testAngRes.getBdgUid()).isEqualTo(DEFAULT_BDG_UID);
        assertThat(testAngRes.getResTyp()).isEqualTo(DEFAULT_RES_TYP);
    }

    @Test
    @Transactional
    void createAngResWithExistingId() throws Exception {
        // Create the AngRes with an existing ID
        angRes.setId(1L);

        int databaseSizeBeforeCreate = angResRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAngResMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angRes)))
            .andExpect(status().isBadRequest());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAngRes() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        // Get all the angResList
        restAngResMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(angRes.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].resCod").value(hasItem(DEFAULT_RES_COD)))
            .andExpect(jsonPath("$.[*].resDsc").value(hasItem(DEFAULT_RES_DSC)))
            .andExpect(jsonPath("$.[*].bdgUid").value(hasItem(DEFAULT_BDG_UID.intValue())))
            .andExpect(jsonPath("$.[*].resTyp").value(hasItem(DEFAULT_RES_TYP.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAngResWithEagerRelationshipsIsEnabled() throws Exception {
        when(angResRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAngResMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(angResRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAngResWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(angResRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAngResMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(angResRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAngRes() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        // Get the angRes
        restAngResMockMvc
            .perform(get(ENTITY_API_URL_ID, angRes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(angRes.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.resCod").value(DEFAULT_RES_COD))
            .andExpect(jsonPath("$.resDsc").value(DEFAULT_RES_DSC))
            .andExpect(jsonPath("$.bdgUid").value(DEFAULT_BDG_UID.intValue()))
            .andExpect(jsonPath("$.resTyp").value(DEFAULT_RES_TYP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAngRes() throws Exception {
        // Get the angRes
        restAngResMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAngRes() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        int databaseSizeBeforeUpdate = angResRepository.findAll().size();

        // Update the angRes
        AngRes updatedAngRes = angResRepository.findById(angRes.getId()).get();
        // Disconnect from session so that the updates on updatedAngRes are not directly saved in db
        em.detach(updatedAngRes);
        updatedAngRes.uid(UPDATED_UID).resCod(UPDATED_RES_COD).resDsc(UPDATED_RES_DSC).bdgUid(UPDATED_BDG_UID).resTyp(UPDATED_RES_TYP);

        restAngResMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAngRes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAngRes))
            )
            .andExpect(status().isOk());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
        AngRes testAngRes = angResList.get(angResList.size() - 1);
        assertThat(testAngRes.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngRes.getResCod()).isEqualTo(UPDATED_RES_COD);
        assertThat(testAngRes.getResDsc()).isEqualTo(UPDATED_RES_DSC);
        assertThat(testAngRes.getBdgUid()).isEqualTo(UPDATED_BDG_UID);
        assertThat(testAngRes.getResTyp()).isEqualTo(UPDATED_RES_TYP);
    }

    @Test
    @Transactional
    void putNonExistingAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(
                put(ENTITY_API_URL_ID, angRes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angRes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(angRes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(angRes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAngResWithPatch() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        int databaseSizeBeforeUpdate = angResRepository.findAll().size();

        // Update the angRes using partial update
        AngRes partialUpdatedAngRes = new AngRes();
        partialUpdatedAngRes.setId(angRes.getId());

        partialUpdatedAngRes.resCod(UPDATED_RES_COD).resTyp(UPDATED_RES_TYP);

        restAngResMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngRes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngRes))
            )
            .andExpect(status().isOk());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
        AngRes testAngRes = angResList.get(angResList.size() - 1);
        assertThat(testAngRes.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testAngRes.getResCod()).isEqualTo(UPDATED_RES_COD);
        assertThat(testAngRes.getResDsc()).isEqualTo(DEFAULT_RES_DSC);
        assertThat(testAngRes.getBdgUid()).isEqualTo(DEFAULT_BDG_UID);
        assertThat(testAngRes.getResTyp()).isEqualTo(UPDATED_RES_TYP);
    }

    @Test
    @Transactional
    void fullUpdateAngResWithPatch() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        int databaseSizeBeforeUpdate = angResRepository.findAll().size();

        // Update the angRes using partial update
        AngRes partialUpdatedAngRes = new AngRes();
        partialUpdatedAngRes.setId(angRes.getId());

        partialUpdatedAngRes
            .uid(UPDATED_UID)
            .resCod(UPDATED_RES_COD)
            .resDsc(UPDATED_RES_DSC)
            .bdgUid(UPDATED_BDG_UID)
            .resTyp(UPDATED_RES_TYP);

        restAngResMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAngRes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAngRes))
            )
            .andExpect(status().isOk());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
        AngRes testAngRes = angResList.get(angResList.size() - 1);
        assertThat(testAngRes.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testAngRes.getResCod()).isEqualTo(UPDATED_RES_COD);
        assertThat(testAngRes.getResDsc()).isEqualTo(UPDATED_RES_DSC);
        assertThat(testAngRes.getBdgUid()).isEqualTo(UPDATED_BDG_UID);
        assertThat(testAngRes.getResTyp()).isEqualTo(UPDATED_RES_TYP);
    }

    @Test
    @Transactional
    void patchNonExistingAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, angRes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angRes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(angRes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAngRes() throws Exception {
        int databaseSizeBeforeUpdate = angResRepository.findAll().size();
        angRes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAngResMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(angRes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AngRes in the database
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAngRes() throws Exception {
        // Initialize the database
        angResRepository.saveAndFlush(angRes);

        int databaseSizeBeforeDelete = angResRepository.findAll().size();

        // Delete the angRes
        restAngResMockMvc
            .perform(delete(ENTITY_API_URL_ID, angRes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AngRes> angResList = angResRepository.findAll();
        assertThat(angResList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
