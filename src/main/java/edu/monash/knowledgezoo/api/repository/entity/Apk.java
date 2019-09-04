package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Apk {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String sha256;

    private Long size;


//   @Relationship(type = "DECLARES")
//   private List<Permission> permissions = new ArrayList<>();
//
//   @Relationship(type = "SIGNED_BY")
//   private List<Certificate> certificates = new ArrayList<>();
//
//   @Relationship(type = "HAS")
//   private List<ApiPackage> packages = new ArrayList<>();
//
//   @Relationship(type = "DEVELOPED_USING")
//   private SDKVersion minimumSDK;

    public Apk() {
    }

    public Apk(Long id, String sha256, String name, Long size) {
        this.id = id;
        this.sha256 = sha256;
        this.name = name;
        this.size = size;
    }

    public Apk(String sha256, String name, Long size) {
        this.sha256 = sha256;
        this.name = name;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

//    public List<Permission> getPermissions() {
//        return permissions;
//    }
//
//    public void setPermissions(List<Permission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public List<Certificate> getCertificates() {
//        return certificates;
//    }
//
//    public void setCertificates(List<Certificate> certificates) {
//        this.certificates = certificates;
//    }
//
//    public List<ApiPackage> getPackages() {
//        return packages;
//    }
//
//    public void setPackages(List<ApiPackage> packages) {
//        this.packages = packages;
//    }
//
//    public SDKVersion getMinimumSDK() {
//        return minimumSDK;
//    }
//
//    public void setMinimumSDK(SDKVersion minimumSDK) {
//        this.minimumSDK = minimumSDK;
//    }


}
