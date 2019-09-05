package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@NodeEntity
public class ReleaseTag {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Index(unique = true)
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Release Date")
    private Date releaseDate;

    @Relationship(type = "DEVELOPED_USING")
    @JsonProperty("SDK Version")
    private SDKVersion sdkVersion;

    @Relationship(type = "CONTAINS")
    @JsonProperty("Signatures")
    private Set<ApiMethodSignature> methodSignatures = new HashSet<>();

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public SDKVersion getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(SDKVersion sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public Set<ApiMethodSignature> getMethodSignatures() {
        return methodSignatures;
    }

    public void setMethodSignatures(Set<ApiMethodSignature> methodSignatures) {
        this.methodSignatures = methodSignatures;
    }

    public void addMethodSignature(ApiMethodSignature methodSignature) {
        this.methodSignatures.add(methodSignature);
        // todo: Duplication handling
    }


}
