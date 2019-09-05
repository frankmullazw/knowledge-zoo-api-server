package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "APK")
public class Apk {
    // todo idea for permissions, split by .permission. to get the owner of the permission

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonProperty("Name")
    private String name;

    @Index(unique = true)
    @JsonProperty("SHA256")
    private String sha256;

    @JsonProperty("Size")
    private Long size;

    @Relationship(type = "DECLARES")
    @JsonProperty("Permissions")
    private Set<Permission> permissions = new HashSet<>();

    @Relationship(type = "DEVELOPED_USING")
    @JsonProperty("Minimum SDK")
    private SDKVersion minimumSDK;

    @Relationship(type = "SIGNED_BY")
    @JsonProperty("Certificate(Owner)")
    private OwnerCertificate ownerCertificate;

    @Relationship(type = "SIGNED_BY")
    @JsonProperty("Certificate(Fingerprint)")
    private FingerprintCertificate fingerprintCertificate;

//   @Relationship(type = "HAS")
//   private List<ApiPackage> packages = new ArrayList<>();
//


    public static final String MINIMUM_SDK_PROPERTY_NAME = "minSDKVersion";

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

    public Apk(String name, String sha256, Long size, Set<Permission> permissions, SDKVersion minimumSDK, OwnerCertificate ownerCertificate, FingerprintCertificate fingerprintCertificate) {
        this.name = name;
        this.sha256 = sha256;
        this.size = size;
        this.permissions = permissions;
        this.minimumSDK = minimumSDK;
        this.ownerCertificate = ownerCertificate;
        this.fingerprintCertificate = fingerprintCertificate;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public SDKVersion getMinimumSDK() {
        return minimumSDK;
    }

    public void setMinimumSDK(SDKVersion minimumSDK) {
        this.minimumSDK = minimumSDK;
    }

    public OwnerCertificate getOwnerCertificate() {
        return ownerCertificate;
    }

    public void setOwnerCertificate(OwnerCertificate ownerCertificate) {
        this.ownerCertificate = ownerCertificate;
    }

    public FingerprintCertificate getFingerprintCertificate() {
        return fingerprintCertificate;
    }

    public void setFingerprintCertificate(FingerprintCertificate fingerprintCertificate) {
        this.fingerprintCertificate = fingerprintCertificate;
    }

    @Override
    public String toString() {
        return "Apk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sha256='" + sha256 + '\'' +
                ", size=" + size +
                ", permissions=" + permissions +
                ", minimumSDK=" + minimumSDK +
                ", ownerCertificate=" + ownerCertificate +
                ", fingerprintCertificate=" + fingerprintCertificate +
                '}';
    }
}
