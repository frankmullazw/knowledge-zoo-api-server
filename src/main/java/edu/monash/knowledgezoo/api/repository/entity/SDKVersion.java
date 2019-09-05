package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;


@NodeEntity
public class SDKVersion {


    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private Integer apiLevel;

    public SDKVersion() {
    }

    public SDKVersion(Integer apiLevel) {
        this.apiLevel = apiLevel;
    }

    public SDKVersion(Long id, Integer apiLevel) {
        this.id = id;
        this.apiLevel = apiLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(Integer apiLevel) {
        this.apiLevel = apiLevel;
    }
}
