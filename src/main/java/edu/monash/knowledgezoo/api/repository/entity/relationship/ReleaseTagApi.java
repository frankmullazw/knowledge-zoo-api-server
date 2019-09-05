package edu.monash.knowledgezoo.api.repository.entity.relationship;

import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.EnumString;

@RelationshipEntity(type = "COMPONENT")
public class ReleaseTagApi {

    public enum State {
        // from the database to allow flexibility

        INTRODUCED,
        REMOVED,
    }


    @Id
    @GeneratedValue
    private Long id;


    @EnumString(value = State.class)
    private State state;

    @StartNode
    private ReleaseTag releaseTag;

    @EndNode
    private Api api;

    public ReleaseTagApi() {
    }

    public ReleaseTagApi(State state, Api api) {
        this.state = state;
        this.api = api;
    }

    public ReleaseTagApi(State state, ReleaseTag releaseTag, Api api) {
        this.state = state;
        this.releaseTag = releaseTag;
        this.api = api;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ReleaseTag getReleaseTag() {
        return releaseTag;
    }

    public void setReleaseTag(ReleaseTag releaseTag) {
        this.releaseTag = releaseTag;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }
}
