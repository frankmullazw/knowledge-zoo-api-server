package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Apk {

    @Id
    @GeneratedValue
    private Long id;

    private String sha256name;

    private String sha1;

    private String md5;

    private String label;

    private Long size;


   @Relationship(type = "DECLARES")
   private List<Permission> permissions = new ArrayList<>();

   @Relationship(type = "SIGNED_BY")
   private List<Certificate> certificates = new ArrayList<>();

   @Relationship(type = "HAS")
   private List<ApiPackage> packages = new ArrayList<>();

   @Relationship(type = "DEVELOPED_USING")
   private SDKVersion minimumSDK;

    public Apk() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSha256name() {
        return sha256name;
    }

    public void setSha256name(String sha256name) {
        this.sha256name = sha256name;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<ApiPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ApiPackage> packages) {
        this.packages = packages;
    }

    public SDKVersion getMinimumSDK() {
        return minimumSDK;
    }

    public void setMinimumSDK(SDKVersion minimumSDK) {
        this.minimumSDK = minimumSDK;
    }
}
