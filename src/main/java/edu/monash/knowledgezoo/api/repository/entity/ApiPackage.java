package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class ApiPackage {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private boolean isLibrary;

}
