package com.example.demo.web.rest;

import com.example.demo.domain.Planetary_system;
import com.example.demo.repository.Planetary_systemRepository;
import com.example.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.demo.domain.Planetary_system}.
 */
@RestController
@RequestMapping("/api")
public class Planetary_systemResource {

    private final Logger log = LoggerFactory.getLogger(Planetary_systemResource.class);

    private static final String ENTITY_NAME = "planetary_system";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Planetary_systemRepository planetary_systemRepository;

    public Planetary_systemResource(Planetary_systemRepository planetary_systemRepository) {
        this.planetary_systemRepository = planetary_systemRepository;
    }

    /**
     * {@code POST  /planetary-systems} : Create a new planetary_system.
     *
     * @param planetary_system the planetary_system to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planetary_system, or with status {@code 400 (Bad Request)} if the planetary_system has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planetary-systems")
    public ResponseEntity<Planetary_system> createPlanetary_system(@RequestBody Planetary_system planetary_system) throws URISyntaxException {
        log.debug("REST request to save Planetary_system : {}", planetary_system);
        if (planetary_system.getId() != null) {
            throw new BadRequestAlertException("A new planetary_system cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Planetary_system result = planetary_systemRepository.save(planetary_system);
        return ResponseEntity.created(new URI("/api/planetary-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planetary-systems} : Updates an existing planetary_system.
     *
     * @param planetary_system the planetary_system to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planetary_system,
     * or with status {@code 400 (Bad Request)} if the planetary_system is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planetary_system couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planetary-systems")
    public ResponseEntity<Planetary_system> updatePlanetary_system(@RequestBody Planetary_system planetary_system) throws URISyntaxException {
        log.debug("REST request to update Planetary_system : {}", planetary_system);
        if (planetary_system.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Planetary_system result = planetary_systemRepository.save(planetary_system);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planetary_system.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /planetary-systems} : get all the planetary_systems.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planetary_systems in body.
     */
    @GetMapping("/planetary-systems")
    public ResponseEntity<List<Planetary_system>> getAllPlanetary_systems(Pageable pageable) {
        log.debug("REST request to get a page of Planetary_systems");
        Page<Planetary_system> page = planetary_systemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planetary-systems/:id} : get the "id" planetary_system.
     *
     * @param id the id of the planetary_system to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planetary_system, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planetary-systems/{id}")
    public ResponseEntity<Planetary_system> getPlanetary_system(@PathVariable Long id) {
        log.debug("REST request to get Planetary_system : {}", id);
        Optional<Planetary_system> planetary_system = planetary_systemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(planetary_system);
    }

    /**
     * {@code DELETE  /planetary-systems/:id} : delete the "id" planetary_system.
     *
     * @param id the id of the planetary_system to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planetary-systems/{id}")
    public ResponseEntity<Void> deletePlanetary_system(@PathVariable Long id) {
        log.debug("REST request to delete Planetary_system : {}", id);
        planetary_systemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
