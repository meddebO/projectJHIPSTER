package com.example.demo.repository;

import com.example.demo.domain.Galaxy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Galaxy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GalaxyRepository extends JpaRepository<Galaxy, Long> {

}
