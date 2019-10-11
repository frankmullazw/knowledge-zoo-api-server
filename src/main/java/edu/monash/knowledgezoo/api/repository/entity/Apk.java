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
    public static final String NAME_PROPERTY_NAME = "name";


    @Index(unique = true)
    @JsonProperty("SHA256")
    private String sha256;
    public static final String SHA256_PROPERTY_NAME = "SHA256";


    @JsonProperty("Size")
    private Long size;
    public static final String SIZE_PROPERTY_NAME = "size";


    @Relationship(type = "DECLARES")
    @JsonProperty("Permissions")
    private Set<Permission> permissions = new HashSet<>();
    public static final String PERMISSIONS_PROPERTY_NAME = "permission";


    @Relationship(type = "DEVELOPED_USING")
    @JsonProperty("Minimum SDK")
    private SDKVersion minimumSDK;
    public static final String MINIMUM_SDK_PROPERTY_NAME = "minSDKVersion";


    @Relationship(type = "SIGNED_BY")
    @JsonProperty("Certificate(Owner)")
    private OwnerCertificate ownerCertificate;
    public static final String OWNER_CERTIFICATE_PROPERTY_NAME = "certificate(owner)";


    @Relationship(type = "SIGNED_BY")
    @JsonProperty("Certificate(Fingerprint)")
    private FingerprintCertificate fingerprintCertificate;
    public static final String FINGERPRINT_CERTIFICATE_PROPERTY_NAME = "certificate(fingerprint)";


    @JsonProperty("Version Code")
    private Integer versionCode;
    public static final String VERSION_CODE_PROPERTY_NAME = "versionCode";


    @JsonProperty("Version Name")
    private String versionName;
    public static final String VERSION_NAME_PROPERTY_NAME = "versionName";


    @Relationship(type = "USES")
    private Set<Activity> activities = new HashSet<>();
    public static final String ACTIVITIES_PROPERTY_NAME = "activity";


    @Relationship(type = "USES")
    private Set<Api> apis = new HashSet<>();
    public static final String APIS_PROPERTY_NAME = "API";


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

    public Apk(String name, String sha256, Long size, Set<Permission> permissions, SDKVersion minimumSDK, OwnerCertificate ownerCertificate, FingerprintCertificate fingerprintCertificate, Integer versionCode, String versionName, Set<Api> apis) {
        this.name = name;
        this.sha256 = sha256;
        this.size = size;
        this.permissions = permissions;
        this.minimumSDK = minimumSDK;
        this.ownerCertificate = ownerCertificate;
        this.fingerprintCertificate = fingerprintCertificate;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apis = apis;
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

    public Set<Api> getApis() {
        return apis;
    }

    public void setApis(Set<Api> apis) {
        this.apis = apis;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
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
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", apis=" + apis.size() +
                '}';
    }

    public void addApi(Api api) {
        if (this.apis == null)
            this.apis = new HashSet<>();

        if (api != null)
            this.apis.add(api);
    }

    public void addActivity(Activity activity) {
        if (this.activities == null)
            this.activities = new HashSet<>();

        if (activity != null)
            this.activities.add(activity);
    }
}
