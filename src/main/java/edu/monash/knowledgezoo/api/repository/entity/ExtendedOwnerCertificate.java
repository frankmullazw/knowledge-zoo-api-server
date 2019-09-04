package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class ExtendedOwnerCertificate {

    @Id
    @GeneratedValue
    private Long id;

    private String companyName;

    private String organizationalUnit;

    private String organization;

    private String locality;

    private String stateProvince;

    private String country;

    //todo: Contants to help with extracting information
    public ExtendedOwnerCertificate() {
    }

    public ExtendedOwnerCertificate(Long id, String companyName, String organizationalUnit, String organization, String locality, String stateProvince, String country) {
        this.id = id;
        this.companyName = companyName;
        this.organizationalUnit = organizationalUnit;
        this.organization = organization;
        this.locality = locality;
        this.stateProvince = stateProvince;
        this.country = country;
    }

    public ExtendedOwnerCertificate(String companyName, String organizationalUnit, String organization, String locality, String stateProvince, String country) {
        this.companyName = companyName;
        this.organizationalUnit = organizationalUnit;
        this.organization = organization;
        this.locality = locality;
        this.stateProvince = stateProvince;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
