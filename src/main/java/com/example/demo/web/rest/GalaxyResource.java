package com.example.demo.web.rest;

import com.example.demo.domain.Galaxy;
import com.example.demo.repository.GalaxyRepository;
import com.example.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.demo.domain.Galaxy}.
 */
@RestController
@RequestMapping("/api")
public class GalaxyResource {

    private final Logger log = LoggerFactory.getLogger(GalaxyResource.class);

    private static final String ENTITY_NAME = "galaxy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GalaxyRepository galaxyRepository;

    public GalaxyResource(GalaxyRepository galaxyRepository) {
        this.galaxyRepository = galaxyRepository;
    }

    /**
     * {@code POST  /galaxies} : Create a new galaxy.
     *
     * @param galaxy the galaxy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new galaxy, or with status {@code 400 (Bad Request)} if the galaxy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/galaxies")
    public ResponseEntity<Galaxy> createGalaxy(@RequestBody Galaxy galaxy) throws URISyntaxException {
        log.debug("REST request to save Galaxy : {}", galaxy);
        if (galaxy.getId() != null) {
            throw new BadRequestAlertException("A new galaxy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Galaxy result = galaxyRepository.save(galaxy);
        return ResponseEntity.created(new URI("/api/galaxies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /galaxies} : Updates an existing galaxy.
     *
     * @param galaxy the galaxy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galaxy,
     * or with status {@code 400 (Bad Request)} if the galaxy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the galaxy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/galaxies")
    public ResponseEntity<Galaxy> updateGalaxy(@RequestBody Galaxy galaxy) throws URISyntaxException {
        log.debug("REST request to update Galaxy : {}", galaxy);
        if (galaxy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Galaxy result = galaxyRepository.save(galaxy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, galaxy.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /galaxies} : get all the galaxies.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of galaxies in body.
     */
    @GetMapping("/galaxies")
    public List<Galaxy> getAllGalaxies() {
        log.debug("REST request to get all Galaxies");
        return galaxyRepository.findAll();
    }

    /**
     * {@code GET  /galaxies/:id} : get the "id" galaxy.
     *
     * @param id the id of the galaxy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the galaxy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/galaxies/{id}")
    public ResponseEntity<Galaxy> getGalaxy(@PathVariable Long id) {
        log.debug("REST request to get Galaxy : {}", id);
        Optional<Galaxy> galaxy = galaxyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(galaxy);
    }

    /**
     * {@code DELETE  /galaxies/:id} : delete the "id" galaxy.
     *
     * @param id the id of the galaxy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/galaxies/{id}")
    public ResponseEntity<Void> deleteGalaxy(@PathVariable Long id) {
        log.debug("REST request to delete Galaxy : {}", id);
        galaxyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
