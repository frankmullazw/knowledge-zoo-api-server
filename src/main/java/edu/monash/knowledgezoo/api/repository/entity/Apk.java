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

}
