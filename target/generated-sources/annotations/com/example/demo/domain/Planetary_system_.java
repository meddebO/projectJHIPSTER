package com.example.demo.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Planetary_system.class)
public abstract class Planetary_system_ {

	public static volatile SingularAttribute<Planetary_system, Galaxy> galaxy_relationship;
	public static volatile SingularAttribute<Planetary_system, String> star;
	public static volatile SingularAttribute<Planetary_system, String> name;
	public static volatile SingularAttribute<Planetary_system, Long> id;
	public static volatile SingularAttribute<Planetary_system, String> galaxy;

	public static final String GALAXY_RELATIONSHIP = "galaxy_relationship";
	public static final String STAR = "star";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String GALAXY = "galaxy";

}

