package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class ReleaseTag {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer releaseDate;

    @Relationship(type = "DEVELOPED_USING")
    private SDKVersion minimumSDK;


}
