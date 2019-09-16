package com.example.demo.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Planet.class)
public abstract class Planet_ {

	public static volatile SingularAttribute<Planet, String> system;
	public static volatile SingularAttribute<Planet, Float> surface;
	public static volatile SingularAttribute<Planet, Planetary_system> planetery_system;
	public static volatile SingularAttribute<Planet, String> name;
	public static volatile SingularAttribute<Planet, Long> id;
	public static volatile SingularAttribute<Planet, Float> radius;

	public static final String SYSTEM = "system";
	public static final String SURFACE = "surface";
	public static final String PLANETERY_SYSTEM = "planetery_system";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String RADIUS = "radius";

}

