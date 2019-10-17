package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.Apk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ApkRepository extends Neo4jRepository<Apk, Long> {

    Apk findBySha256(@Param("sha256") String sha256);

    Collection<Apk> findByNameLike(@Param("name") String name);

    @Query(value = "MATCH (p:Permission)<-[:DECLARES]-(apk:APK) WHERE p.genericName = {0} RETURN DISTINCT apk",
            countQuery = "MATCH (p:Permission)<-[:DECLARES]-(apk:APK) WHERE p.id = {0} RETURN COUNT(DISTINCT apk)")
    Page<Apk> findByPermissionGenericName(String genericName, Pageable page);

    @Query(value = "MATCH (api:API)<-[:USES]-(apk:APK) WHERE api.name = {0} RETURN DISTINCT apk",
            countQuery = "MATCH (api:API)<-[:USES]-(apk:APK) WHERE api.name CONTAINS {0} RETURN COUNT(DISTINCT apk)")
    Page<Apk> findByApiName(String name, Pageable page);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
