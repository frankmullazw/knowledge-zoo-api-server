package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Permission {

     @Id
     @GeneratedValue
     private Long id;

     private String name;

     private String genericName;

}
