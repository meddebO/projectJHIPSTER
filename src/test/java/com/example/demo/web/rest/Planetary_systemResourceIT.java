package com.example.demo.web.rest;

import com.example.demo.JhipsterAppApp;
import com.example.demo.domain.Planetary_system;
import com.example.demo.repository.Planetary_systemRepository;
import com.example.demo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.demo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link Planetary_systemResource} REST controller.
 */
@SpringBootTest(classes = JhipsterAppApp.class)
public class Planetary_systemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STAR = "AAAAAAAAAA";
    private static final String UPDATED_STAR = "BBBBBBBBBB";

    private static final String DEFAULT_GALAXY = "AAAAAAAAAA";
    private static final String UPDATED_GALAXY = "BBBBBBBBBB";

    @Autowired
    private Planetary_systemRepository planetary_systemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPlanetary_systemMockMvc;

    private Planetary_system planetary_system;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Planetary_systemResource planetary_systemResource = new Planetary_systemResource(planetary_systemRepository);
        this.restPlanetary_systemMockMvc = MockMvcBuilders.standaloneSetup(planetary_systemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planetary_system createEntity(EntityManager em) {
        Planetary_system planetary_system = new Planetary_system()
            .name(DEFAULT_NAME)
            .star(DEFAULT_STAR)
            .galaxy(DEFAULT_GALAXY);
        return planetary_system;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planetary_system createUpdatedEntity(EntityManager em) {
        Planetary_system planetary_system = new Planetary_system()
            .name(UPDATED_NAME)
            .star(UPDATED_STAR)
            .galaxy(UPDATED_GALAXY);
        return planetary_system;
    }

    @BeforeEach
    public void initTest() {
        planetary_system = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanetary_system() throws Exception {
        int databaseSizeBeforeCreate = planetary_systemRepository.findAll().size();

        // Create the Planetary_system
        restPlanetary_systemMockMvc.perform(post("/api/planetary-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planetary_system)))
            .andExpect(status().isCreated());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systemList = planetary_systemRepository.findAll();
        assertThat(planetary_systemList).hasSize(databaseSizeBeforeCreate + 1);
        Planetary_system testPlanetary_system = planetary_systemList.get(planetary_systemList.size() - 1);
        assertThat(testPlanetary_system.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanetary_system.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testPlanetary_system.getGalaxy()).isEqualTo(DEFAULT_GALAXY);
    }

    @Test
    @Transactional
    public void createPlanetary_systemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planetary_systemRepository.findAll().size();

        // Create the Planetary_system with an existing ID
        planetary_system.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanetary_systemMockMvc.perform(post("/api/planetary-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planetary_system)))
            .andExpect(status().isBadRequest());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systemList = planetary_systemRepository.findAll();
        assertThat(planetary_systemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlanetary_systems() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        // Get all the planetary_systemList
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planetary_system.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR.toString())))
            .andExpect(jsonPath("$.[*].galaxy").value(hasItem(DEFAULT_GALAXY.toString())));
    }
    
    @Test
    @Transactional
    public void getPlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        // Get the planetary_system
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems/{id}", planetary_system.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planetary_system.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR.toString()))
            .andExpect(jsonPath("$.galaxy").value(DEFAULT_GALAXY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanetary_system() throws Exception {
        // Get the planetary_system
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        int databaseSizeBeforeUpdate = planetary_systemRepository.findAll().size();

        // Update the planetary_system
        Planetary_system updatedPlanetary_system = planetary_systemRepository.findById(planetary_system.getId()).get();
        // Disconnect from session so that the updates on updatedPlanetary_system are not directly saved in db
        em.detach(updatedPlanetary_system);
        updatedPlanetary_system
            .name(UPDATED_NAME)
            .star(UPDATED_STAR)
            .galaxy(UPDATED_GALAXY);

        restPlanetary_systemMockMvc.perform(put("/api/planetary-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanetary_system)))
            .andExpect(status().isOk());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systemList = planetary_systemRepository.findAll();
        assertThat(planetary_systemList).hasSize(databaseSizeBeforeUpdate);
        Planetary_system testPlanetary_system = planetary_systemList.get(planetary_systemList.size() - 1);
        assertThat(testPlanetary_system.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanetary_system.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testPlanetary_system.getGalaxy()).isEqualTo(UPDATED_GALAXY);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanetary_system() throws Exception {
        int databaseSizeBeforeUpdate = planetary_systemRepository.findAll().size();

        // Create the Planetary_system

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanetary_systemMockMvc.perform(put("/api/planetary-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planetary_system)))
            .andExpect(status().isBadRequest());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systemList = planetary_systemRepository.findAll();
        assertThat(planetary_systemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        int databaseSizeBeforeDelete = planetary_systemRepository.findAll().size();

        // Delete the planetary_system
        restPlanetary_systemMockMvc.perform(delete("/api/planetary-systems/{id}", planetary_system.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planetary_system> planetary_systemList = planetary_systemRepository.findAll();
        assertThat(planetary_systemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planetary_system.class);
        Planetary_system planetary_system1 = new Planetary_system();
        planetary_system1.setId(1L);
        Planetary_system planetary_system2 = new Planetary_system();
        planetary_system2.setId(planetary_system1.getId());
        assertThat(planetary_system1).isEqualTo(planetary_system2);
        planetary_system2.setId(2L);
        assertThat(planetary_system1).isNotEqualTo(planetary_system2);
        planetary_system1.setId(null);
        assertThat(planetary_system1).isNotEqualTo(planetary_system2);
    }
}
