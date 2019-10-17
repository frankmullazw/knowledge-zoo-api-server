package edu.monash.knowledgezoo.api.repository.entity.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.repository.entity.ApiPackage;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import org.neo4j.ogm.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RelationshipEntity(type = "APK_API_PACKAGE")
public class ApiPackageRelationship {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @StartNode
    @JsonIgnore
    private Apk apk;

    @Property
    private Set<String> packages = new HashSet<>();

    @EndNode
    private Api api;

    public ApiPackageRelationship() {
    }

    public ApiPackageRelationship(Apk apk, Api api) {
        this.apk = apk;
        this.api = api;
    }

    public ApiPackageRelationship(Apk apk, Api api, Set<ApiPackage> packages) {
        this.apk = apk;
        this.addAllApiPackages(packages);
        this.api = api;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apk getApk() {
        return apk;
    }

    public void setApk(Apk apk) {
        this.apk = apk;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Set<String> getPackages() {
        return packages;
    }

    public void setPackages(Set<String> packages) {
        this.packages = packages;
    }

    public void addPackage(ApiPackage newPackage) {
        if (newPackage != null)
            this.packages.add(newPackage.getName());
    }

    public void addPackage(String newPackage) {
        if (newPackage != null)
            this.packages.add(newPackage);
    }

    public void addAllApiPackages(Collection<ApiPackage> packages) {
        for (ApiPackage apiPackage : packages)
            this.addPackage(apiPackage);
    }

    public void addAllStringPackages(Collection<String> packages) {
        this.packages.addAll(packages);
    }
}
