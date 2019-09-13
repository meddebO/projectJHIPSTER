package com.example.demo.web.rest;

import com.example.demo.JhipsterAppApp;
import com.example.demo.domain.Galaxy;
import com.example.demo.repository.GalaxyRepository;
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
 * Integration tests for the {@link GalaxyResource} REST controller.
 */
@SpringBootTest(classes = JhipsterAppApp.class)
public class GalaxyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private GalaxyRepository galaxyRepository;

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

    private MockMvc restGalaxyMockMvc;

    private Galaxy galaxy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GalaxyResource galaxyResource = new GalaxyResource(galaxyRepository);
        this.restGalaxyMockMvc = MockMvcBuilders.standaloneSetup(galaxyResource)
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
    public static Galaxy createEntity(EntityManager em) {
        Galaxy galaxy = new Galaxy()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE);
        return galaxy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Galaxy createUpdatedEntity(EntityManager em) {
        Galaxy galaxy = new Galaxy()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);
        return galaxy;
    }

    @BeforeEach
    public void initTest() {
        galaxy = createEntity(em);
    }

    @Test
    @Transactional
    public void createGalaxy() throws Exception {
        int databaseSizeBeforeCreate = galaxyRepository.findAll().size();

        // Create the Galaxy
        restGalaxyMockMvc.perform(post("/api/galaxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galaxy)))
            .andExpect(status().isCreated());

        // Validate the Galaxy in the database
        List<Galaxy> galaxyList = galaxyRepository.findAll();
        assertThat(galaxyList).hasSize(databaseSizeBeforeCreate + 1);
        Galaxy testGalaxy = galaxyList.get(galaxyList.size() - 1);
        assertThat(testGalaxy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGalaxy.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createGalaxyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = galaxyRepository.findAll().size();

        // Create the Galaxy with an existing ID
        galaxy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGalaxyMockMvc.perform(post("/api/galaxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galaxy)))
            .andExpect(status().isBadRequest());

        // Validate the Galaxy in the database
        List<Galaxy> galaxyList = galaxyRepository.findAll();
        assertThat(galaxyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGalaxies() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        // Get all the galaxyList
        restGalaxyMockMvc.perform(get("/api/galaxies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(galaxy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        // Get the galaxy
        restGalaxyMockMvc.perform(get("/api/galaxies/{id}", galaxy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(galaxy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGalaxy() throws Exception {
        // Get the galaxy
        restGalaxyMockMvc.perform(get("/api/galaxies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        int databaseSizeBeforeUpdate = galaxyRepository.findAll().size();

        // Update the galaxy
        Galaxy updatedGalaxy = galaxyRepository.findById(galaxy.getId()).get();
        // Disconnect from session so that the updates on updatedGalaxy are not directly saved in db
        em.detach(updatedGalaxy);
        updatedGalaxy
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE);

        restGalaxyMockMvc.perform(put("/api/galaxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGalaxy)))
            .andExpect(status().isOk());

        // Validate the Galaxy in the database
        List<Galaxy> galaxyList = galaxyRepository.findAll();
        assertThat(galaxyList).hasSize(databaseSizeBeforeUpdate);
        Galaxy testGalaxy = galaxyList.get(galaxyList.size() - 1);
        assertThat(testGalaxy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGalaxy.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingGalaxy() throws Exception {
        int databaseSizeBeforeUpdate = galaxyRepository.findAll().size();

        // Create the Galaxy

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGalaxyMockMvc.perform(put("/api/galaxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(galaxy)))
            .andExpect(status().isBadRequest());

        // Validate the Galaxy in the database
        List<Galaxy> galaxyList = galaxyRepository.findAll();
        assertThat(galaxyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        int databaseSizeBeforeDelete = galaxyRepository.findAll().size();

        // Delete the galaxy
        restGalaxyMockMvc.perform(delete("/api/galaxies/{id}", galaxy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Galaxy> galaxyList = galaxyRepository.findAll();
        assertThat(galaxyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Galaxy.class);
        Galaxy galaxy1 = new Galaxy();
        galaxy1.setId(1L);
        Galaxy galaxy2 = new Galaxy();
        galaxy2.setId(galaxy1.getId());
        assertThat(galaxy1).isEqualTo(galaxy2);
        galaxy2.setId(2L);
        assertThat(galaxy1).isNotEqualTo(galaxy2);
        galaxy1.setId(null);
        assertThat(galaxy1).isNotEqualTo(galaxy2);
    }
}
