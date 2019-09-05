package edu.monash.knowledgezoo.api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Certificate(Owner)")
public class OwnerCertificate {

    public static final String PROPERTY_NAME = "certificate(fingerprint)";

    // Contants to help with extracting information

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonProperty("Common Name")
    private String commonName;
    public static final String COMMON_NAME_PROPERTY_NAME = "Common Name";
    public static final String COMMON_NAME_SHORT_PROPERTY_NAME = "CN";

    @JsonProperty("Organization Unit")
    private String organizationalUnit;
    public static final String ORGANIZATIONAL_UNIT_PROPERTY_NAME = "Organizational Unit";
    public static final String ORGANIZATIONAL_UNIT_SHORT_PROPERTY_NAME = "OU";

    @JsonProperty("Organization")
    private String organization;
    public static final String ORGANIZATION_PROPERTY_NAME = "Organization";
    public static final String ORGANIZATION_SHORT_PROPERTY_NAME = "O";

    @JsonProperty("Locality")
    private String locality;
    public static final String LOCALITY_PROPERTY_NAME = "Locality";
    public static final String LOCALITY_SHORT_PROPERTY_NAME = "L";

    @JsonProperty("State/Province")
    private String stateProvince;
    public static final String STATE_PROVINCE_PROPERTY_NAME = "State/Province";
    public static final String STATE_PROVINCE_SHORT_PROPERTY_NAME = "ST";

    @JsonProperty("Country")
    private String country;
    public static final String COUNTRY_PROPERTY_NAME = "Country";
    public static final String COUNTRY_SHORT_PROPERTY_NAME = "C";

    @Index(unique = true)
    @JsonIgnore
    private String fullCertificate;

    public OwnerCertificate() {
    }

    public OwnerCertificate(Long id, String commonName, String organizationalUnit, String organization, String locality, String stateProvince, String country) {
        this.id = id;
        this.commonName = commonName;
        this.organizationalUnit = organizationalUnit;
        this.organization = organization;
        this.locality = locality;
        this.stateProvince = stateProvince;
        this.country = country;
    }

    public OwnerCertificate(String commonName, String organizationalUnit, String organization, String locality, String stateProvince, String country) {
        this.commonName = commonName;
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

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
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

    public String getFullCertificate() {
        return fullCertificate;
    }

    public void setFullCertificate(String fullCertificate) {
        this.fullCertificate = fullCertificate;
    }


    /**
     * Uses the full owner certificate string and extracts the properties into their respective fields
     *
     * @param certificateString full owner certificate string
     * @return Certificate objects with assorted values in fields
     */
    public static OwnerCertificate generateFromFullCertificate(String certificateString) {
        OwnerCertificate certificate = new OwnerCertificate();

        // Process the certificate String
        String[] certParts = certificateString.split(",");

        for (String property : certParts) {
            String[] kvPair = property.split(":");
            // Clean up
            // todo: Check cases where there are more than two in kv pairs(value with : symbol)
            kvPair[0] = kvPair[0].trim();
            kvPair[1] = kvPair[1].trim();

            // Determine properties and assign b
            switch (kvPair[0]) {
                case COMMON_NAME_PROPERTY_NAME:
                case COMMON_NAME_SHORT_PROPERTY_NAME:
                    certificate.setCommonName(kvPair[1]);
                    break;
                case ORGANIZATION_PROPERTY_NAME:
                case ORGANIZATION_SHORT_PROPERTY_NAME:
                    certificate.setOrganization(kvPair[1]);
                    break;
                case ORGANIZATIONAL_UNIT_PROPERTY_NAME:
                case ORGANIZATIONAL_UNIT_SHORT_PROPERTY_NAME:
                    certificate.setOrganizationalUnit(kvPair[1]);
                    break;
                case LOCALITY_PROPERTY_NAME:
                case LOCALITY_SHORT_PROPERTY_NAME:
                    certificate.setLocality(kvPair[1]);
                    break;
                case STATE_PROVINCE_PROPERTY_NAME:
                case STATE_PROVINCE_SHORT_PROPERTY_NAME:
                    certificate.setStateProvince(kvPair[1]);
                    break;
                case COUNTRY_PROPERTY_NAME:
                case COUNTRY_SHORT_PROPERTY_NAME:
                    certificate.setCountry(kvPair[1]);
                    break;
                default:
                    System.out.println("No Match");

            }
        }

        certificate.setFullCertificate(certificateString);
        return certificate;
    }

    @Override
    public String toString() {
        return "OwnerCertificate{" +
                "id=" + id +
                ", commonName='" + commonName + '\'' +
                ", organizationalUnit='" + organizationalUnit + '\'' +
                ", organization='" + organization + '\'' +
                ", locality='" + locality + '\'' +
                ", stateProvince='" + stateProvince + '\'' +
                ", country='" + country + '\'' +
                ", fullCertificate='" + fullCertificate + '\'' +
                '}';
    }
}
