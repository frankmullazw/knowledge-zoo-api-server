package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class FingerprintCertificate {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    public FingerprintCertificate() {
    }

    public FingerprintCertificate(String value) {
        this.value = value;
    }

    public FingerprintCertificate(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
