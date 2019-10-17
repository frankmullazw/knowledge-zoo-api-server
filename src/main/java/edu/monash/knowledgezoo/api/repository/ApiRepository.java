package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.Api;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ApiRepository extends Neo4jRepository<Api, Long> {

    Api findByName(@Param("name") String name);

    @Query("MATCH (:APK)-[:APK_API_PACKAGE]->(api:API)<-[r:CONTAINS]-(:Tag) where api.name CONTAINS {name} RETURN api, r limit 1")
    Api findByNameLike(@Param("name") String name);

    @Query("MATCH (api:API)<-[r:APK_API_PACKAGE]-(:APK) RETURN DISTINCT api, COUNT(r) ORDER BY COUNT(r) DESC LIMIT 10")
    Collection<Api> getTop10Apis();

//    ApiPackage findByGenericName(@Param("genericName") String genericName);

//    Collection<ApiPackage> findByGenericNameLike(@Param("genericName") String genericName);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
