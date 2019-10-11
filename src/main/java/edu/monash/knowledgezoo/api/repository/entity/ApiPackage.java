package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Package")
public class ApiPackage {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Index(unique = true)
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Is Library")
    private boolean isLibrary;

//    @Relationship(type = "CONTAINS", direction = Relationship.INCOMING)
//    private Set<Api> apis = new HashSet<>();

    public ApiPackage() {
    }

    public ApiPackage(String name) {
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

    public boolean isLibrary() {
        return isLibrary;
    }

    public void setLibrary(boolean library) {
        isLibrary = library;
    }

//    public Set<Api> getApis() {
//        return apis;
//    }
//
//    public void addApis(Api api) {
//        if (api != null) {
//            if (this.apis == null)
//                this.apis = new HashSet<>();
//            this.apis.add(api);
//        }
//    }
//
//    public void setApis(Set<Api> apis) {
//        this.apis = apis;
//    }


}
