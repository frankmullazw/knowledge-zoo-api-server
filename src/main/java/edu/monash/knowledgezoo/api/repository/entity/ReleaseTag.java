package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.monash.knowledgezoo.api.repository.entity.relationship.ReleaseTagApi;
import org.neo4j.ogm.annotation.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@NodeEntity(label = "Tag")
public class ReleaseTag {

    public static final String TAG_PREFIX = "refs/tags/";

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Index(unique = true)
    @JsonProperty("Name")
    private String name;

//    @JsonProperty("Release Date")
    @JsonIgnore
    private Date releaseDate;

    @Relationship(type = "DEVELOPED_USING")
//    @JsonProperty("SDK Version")
    @JsonIgnore
    private SDKVersion sdkVersion;

    @Relationship(type = "TagToTag")
    @JsonIgnore
    private ReleaseTag nextTag;

    @JsonIgnore
    private Set<ReleaseTagApi> releaseTagApis = new HashSet<>();

    public ReleaseTag() {
    }

    public ReleaseTag(String name) {
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

    public Set<ReleaseTagApi> getReleaseTagApis() {
        return releaseTagApis;
    }

    public void setReleaseTagApis(Set<ReleaseTagApi> releaseTagApis) {
        this.releaseTagApis = releaseTagApis;
    }

    public void addReleaseApi(ReleaseTagApi releaseTagApi) {
        // todo: Duplication handling
        if (releaseTagApi != null) {
            releaseTagApi.setReleaseTag(this);
            this.releaseTagApis.add(releaseTagApi);
        }
    }

    public void addReleaseApi(Api api, ReleaseTagApi.State state) {
        // todo: Duplication handling
        if (api != null && state != null) {
            this.releaseTagApis.add(new ReleaseTagApi(state, this, api));
        }
    }

    public ReleaseTag getNextTag() {
        return nextTag;
    }

    public void setNextTag(ReleaseTag nextTag) {
        this.nextTag = nextTag;
    }

    @Override
    public String toString() {
        return "ReleaseTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", sdkVersion=" + sdkVersion +
                ", releaseTagApis=" + releaseTagApis +
                '}';
    }
}
