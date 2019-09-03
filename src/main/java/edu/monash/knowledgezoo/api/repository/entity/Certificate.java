package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Certificate {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    //todo might need to make this its own entity
    private String owner;
}
