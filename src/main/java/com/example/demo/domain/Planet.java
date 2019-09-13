package com.example.demo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Planet.
 */
@Entity
@Table(name = "planet")
public class Planet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surface")
    private Float surface;

    @Column(name = "radius")
    private Float radius;

    @Column(name = "system")
    private String system;

    @ManyToOne
    private Planetary_system planetery_system;

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

    public Planet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSurface() {
        return surface;
    }

    public Planet surface(Float surface) {
        this.surface = surface;
        return this;
    }

    public void setSurface(Float surface) {
        this.surface = surface;
    }

    public Float getRadius() {
        return radius;
    }

    public Planet radius(Float radius) {
        this.radius = radius;
        return this;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getSystem() {
        return system;
    }

    public Planet system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Planetary_system getPlanetery_system() {
        return planetery_system;
    }

    public Planet planetery_system(Planetary_system planetery_system) {
        this.planetery_system = planetery_system;
        return this;
    }

    public void setPlanetery_system(Planetary_system planetery_system) {
        this.planetery_system = planetery_system;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planet)) {
            return false;
        }
        return id != null && id.equals(((Planet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Planet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surface=" + getSurface() +
            ", radius=" + getRadius() +
            ", system='" + getSystem() + "'" +
            "}";
    }
}
