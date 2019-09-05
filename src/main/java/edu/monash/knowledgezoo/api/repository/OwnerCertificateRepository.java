package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.OwnerCertificate;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface OwnerCertificateRepository extends Neo4jRepository<OwnerCertificate, Long> {

    OwnerCertificate findByFullCertificate(@Param("fullCertificate") String fullCertificate);

    Collection<OwnerCertificate> findByCommonName(@Param("commonName") String commonName);

    Collection<OwnerCertificate> findByCommonNameLike(@Param("commonName") String commonName);

    Collection<OwnerCertificate> findByOrganizationalUnit(@Param("organizationalUnit") String organizationalUnit);

    Collection<OwnerCertificate> findByOrganizationalUnitLike(@Param("organizationalUnit") String organizationalUnit);

    Collection<OwnerCertificate> findByOrganization(@Param("organisation") String organisation);

    Collection<OwnerCertificate> findByOrganizationLike(@Param("organisation") String organisation);

    Collection<OwnerCertificate> findByLocality(@Param("locality") String locality);

    Collection<OwnerCertificate> findByLocalityLike(@Param("locality") String locality);

    Collection<OwnerCertificate> findByStateProvince(@Param("stateProvince") String stateProvince);

    Collection<OwnerCertificate> findByStateProvinceLike(@Param("stateProvince") String stateProvince);

    Collection<OwnerCertificate> findByCountry(@Param("country") String country);

    Collection<OwnerCertificate> findByCountryLike(@Param("country") String country);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
