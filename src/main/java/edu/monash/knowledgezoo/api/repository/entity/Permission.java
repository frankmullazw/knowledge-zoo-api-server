package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Permission")
public class Permission {

    // todo Add permission provider

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Index(unique = true)
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Generic Name")
    private String genericName;

    public Permission() {
    }

    public Permission(String name) {
        this.setName(name);
    }

    public Permission(String name, String genericName) {
        this.name = name;
        this.genericName = genericName;
    }

    public Permission(Long id, String name, String genericName) {
        this.id = id;
        this.name = name;
        this.genericName = genericName;
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

        if (this.genericName == null || this.genericName.equals(""))
            // set a new generic name
            this.setGenericName(constructGenericName(this.name));
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    /**
     * This method constructs a generic name from the given permission name
     * input: android.permission.WRITE_SYNC_SETTINGS, output: Write Sync Settings
     *
     * @param fullPermission Full android permission name e.g android.permission.WRITE_SYNC_SETTINGS
     * @return Generic permission name in format easy for human understanding
     */
    private String constructGenericName(String fullPermission) {
        if (fullPermission == null) {
            System.out.println("Null fullPermission input in constructGenericName");
            return null;
        } else if (fullPermission.equals("")) {
            System.out.println("Empty fullPermission input in constructGenericName");
            return "";
        } else {
            // set a new generic name
            // Splits the permission by peroid and then uses the splits using the semi colons while
            // only leaving the first letter captilized
            String[] permParts = fullPermission.split("\\.");
            StringBuilder tempGenericName = new StringBuilder("");
            for (String word : permParts[permParts.length - 1].split("_")) {
                tempGenericName.append(word.charAt(0));
                tempGenericName.append(word.substring(1).toLowerCase());
                tempGenericName.append(" ");
            }
            tempGenericName.setLength(tempGenericName.length() - 1);

            return tempGenericName.toString();
        }
    }
}
