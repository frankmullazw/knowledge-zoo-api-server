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


    public ReleaseTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public SDKVersion getMinimumSDK() {
        return minimumSDK;
    }

    public void setMinimumSDK(SDKVersion minimumSDK) {
        this.minimumSDK = minimumSDK;
    }
}
