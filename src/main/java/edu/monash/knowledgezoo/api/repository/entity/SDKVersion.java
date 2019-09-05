package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;


@NodeEntity(label = "SDKversion")
public class SDKVersion {


    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Index(unique = true)
    @JsonProperty("Api Level")
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
