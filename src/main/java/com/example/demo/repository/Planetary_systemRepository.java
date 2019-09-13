package com.example.demo.repository;

import com.example.demo.domain.Planetary_system;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Planetary_system entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Planetary_systemRepository extends JpaRepository<Planetary_system, Long> {

}
