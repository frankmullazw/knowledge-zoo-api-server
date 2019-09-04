package edu.monash.knowledgezoo.api.repository.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Permission {

     @Id
     @GeneratedValue
     private Long id;

     private String name;

     private String genericName;

     public Permission() {
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

     public String getGenericName() {
          return genericName;
     }

     public void setGenericName(String genericName) {
          this.genericName = genericName;
     }
}
