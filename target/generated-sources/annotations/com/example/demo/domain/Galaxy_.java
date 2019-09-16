package com.example.demo.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Galaxy.class)
public abstract class Galaxy_ {

	public static volatile SingularAttribute<Galaxy, String> name;
	public static volatile SingularAttribute<Galaxy, Long> id;
	public static volatile SingularAttribute<Galaxy, String> type;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String TYPE = "type";

}

