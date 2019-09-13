package com.example.demo.repository;

import com.example.demo.domain.Planet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Planet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

}
