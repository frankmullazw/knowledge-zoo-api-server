package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "API")
public class Api {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    // todo: Really think about if this should be unique or not
    // Map the relationship between releaseTag
    @Index(unique = true)
    @JsonProperty("Name")
    private String name;

    @Relationship(type = "USED_BY")
    private Set<ApiPackage> packages = new HashSet<>();

    public Api() {
    }

    public Api(String name) {
        this.name = name;
    }

    public Api(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<ApiPackage> getPackages() {
        return packages;
    }

    public void addPackage(ApiPackage newPackage) {
        if (newPackage != null) {
            if (this.packages == null)
                this.packages = new HashSet<>();
            this.packages.add(newPackage);
        }
    }

    public void setPackages(Set<ApiPackage> packages) {
        this.packages = packages;
    }
}
