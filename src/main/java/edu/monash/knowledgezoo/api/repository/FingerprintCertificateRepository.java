package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.FingerprintCertificate;
import edu.monash.knowledgezoo.api.repository.entity.Permission;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface FingerprintCertificateRepository extends Neo4jRepository<FingerprintCertificate, Long> {

    FingerprintCertificate findByName(@Param("name") String name);

    Collection<FingerprintCertificate> findByNameLike(@Param("name") String name);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
