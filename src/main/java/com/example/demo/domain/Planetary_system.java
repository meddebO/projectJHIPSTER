package com.example.demo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Planetary_system.
 */
@Entity
@Table(name = "planetary_system")
public class Planetary_system implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "star")
    private String star;

    @Column(name = "galaxy")
    private String galaxy;

    @ManyToOne
    private Galaxy galaxy_relationship;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Planetary_system name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public Planetary_system star(String star) {
        this.star = star;
        return this;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public Planetary_system galaxy(String galaxy) {
        this.galaxy = galaxy;
        return this;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
    }

    public Galaxy getGalaxy_relationship() {
        return galaxy_relationship;
    }

    public Planetary_system galaxy_relationship(Galaxy galaxy) {
        this.galaxy_relationship = galaxy;
        return this;
    }

    public void setGalaxy_relationship(Galaxy galaxy) {
        this.galaxy_relationship = galaxy;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planetary_system)) {
            return false;
        }
        return id != null && id.equals(((Planetary_system) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Planetary_system{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", star='" + getStar() + "'" +
            ", galaxy='" + getGalaxy() + "'" +
            "}";
    }
}
