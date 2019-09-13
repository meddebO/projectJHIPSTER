package com.example.demo.web.rest;

import com.example.demo.JhipsterAppApp;
import com.example.demo.domain.Planet;
import com.example.demo.repository.PlanetRepository;
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
 * Integration tests for the {@link PlanetResource} REST controller.
 */
@SpringBootTest(classes = JhipsterAppApp.class)
public class PlanetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_SURFACE = 1F;
    private static final Float UPDATED_SURFACE = 2F;
    private static final Float SMALLER_SURFACE = 1F - 1F;

    private static final Float DEFAULT_RADIUS = 1F;
    private static final Float UPDATED_RADIUS = 2F;
    private static final Float SMALLER_RADIUS = 1F - 1F;

    private static final String DEFAULT_SYSTEM = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM = "BBBBBBBBBB";

    @Autowired
    private PlanetRepository planetRepository;

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

    private MockMvc restPlanetMockMvc;

    private Planet planet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanetResource planetResource = new PlanetResource(planetRepository);
        this.restPlanetMockMvc = MockMvcBuilders.standaloneSetup(planetResource)
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
    public static Planet createEntity(EntityManager em) {
        Planet planet = new Planet()
            .name(DEFAULT_NAME)
            .surface(DEFAULT_SURFACE)
            .radius(DEFAULT_RADIUS)
            .system(DEFAULT_SYSTEM);
        return planet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planet createUpdatedEntity(EntityManager em) {
        Planet planet = new Planet()
            .name(UPDATED_NAME)
            .surface(UPDATED_SURFACE)
            .radius(UPDATED_RADIUS)
            .system(UPDATED_SYSTEM);
        return planet;
    }

    @BeforeEach
    public void initTest() {
        planet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanet() throws Exception {
        int databaseSizeBeforeCreate = planetRepository.findAll().size();

        // Create the Planet
        restPlanetMockMvc.perform(post("/api/planets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planet)))
            .andExpect(status().isCreated());

        // Validate the Planet in the database
        List<Planet> planetList = planetRepository.findAll();
        assertThat(planetList).hasSize(databaseSizeBeforeCreate + 1);
        Planet testPlanet = planetList.get(planetList.size() - 1);
        assertThat(testPlanet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanet.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testPlanet.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testPlanet.getSystem()).isEqualTo(DEFAULT_SYSTEM);
    }

    @Test
    @Transactional
    public void createPlanetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planetRepository.findAll().size();

        // Create the Planet with an existing ID
        planet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanetMockMvc.perform(post("/api/planets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planet)))
            .andExpect(status().isBadRequest());

        // Validate the Planet in the database
        List<Planet> planetList = planetRepository.findAll();
        assertThat(planetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlanets() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        // Get all the planetList
        restPlanetMockMvc.perform(get("/api/planets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surface").value(hasItem(DEFAULT_SURFACE.doubleValue())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.toString())));
    }
    
    @Test
    @Transactional
    public void getPlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        // Get the planet
        restPlanetMockMvc.perform(get("/api/planets/{id}", planet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surface").value(DEFAULT_SURFACE.doubleValue()))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS.doubleValue()))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanet() throws Exception {
        // Get the planet
        restPlanetMockMvc.perform(get("/api/planets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        int databaseSizeBeforeUpdate = planetRepository.findAll().size();

        // Update the planet
        Planet updatedPlanet = planetRepository.findById(planet.getId()).get();
        // Disconnect from session so that the updates on updatedPlanet are not directly saved in db
        em.detach(updatedPlanet);
        updatedPlanet
            .name(UPDATED_NAME)
            .surface(UPDATED_SURFACE)
            .radius(UPDATED_RADIUS)
            .system(UPDATED_SYSTEM);

        restPlanetMockMvc.perform(put("/api/planets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanet)))
            .andExpect(status().isOk());

        // Validate the Planet in the database
        List<Planet> planetList = planetRepository.findAll();
        assertThat(planetList).hasSize(databaseSizeBeforeUpdate);
        Planet testPlanet = planetList.get(planetList.size() - 1);
        assertThat(testPlanet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanet.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testPlanet.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testPlanet.getSystem()).isEqualTo(UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanet() throws Exception {
        int databaseSizeBeforeUpdate = planetRepository.findAll().size();

        // Create the Planet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanetMockMvc.perform(put("/api/planets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planet)))
            .andExpect(status().isBadRequest());

        // Validate the Planet in the database
        List<Planet> planetList = planetRepository.findAll();
        assertThat(planetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        int databaseSizeBeforeDelete = planetRepository.findAll().size();

        // Delete the planet
        restPlanetMockMvc.perform(delete("/api/planets/{id}", planet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planet> planetList = planetRepository.findAll();
        assertThat(planetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planet.class);
        Planet planet1 = new Planet();
        planet1.setId(1L);
        Planet planet2 = new Planet();
        planet2.setId(planet1.getId());
        assertThat(planet1).isEqualTo(planet2);
        planet2.setId(2L);
        assertThat(planet1).isNotEqualTo(planet2);
        planet1.setId(null);
        assertThat(planet1).isNotEqualTo(planet2);
    }
}
