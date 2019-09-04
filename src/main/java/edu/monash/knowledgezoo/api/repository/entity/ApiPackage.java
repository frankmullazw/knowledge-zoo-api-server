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

    public ApiPackage() {
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

    public boolean isLibrary() {
        return isLibrary;
    }

    public void setLibrary(boolean library) {
        isLibrary = library;
    }
}
