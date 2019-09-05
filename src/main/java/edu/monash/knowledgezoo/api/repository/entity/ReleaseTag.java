package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.sql.Date;


@NodeEntity
public class ReleaseTag {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Date releaseDate;

    @Relationship(type = "DEVELOPED_USING")
    private SDKVersion sdkVersion;

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
}
