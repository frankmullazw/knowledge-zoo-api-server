package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.Api;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ApiRepository extends Neo4jRepository<Api, Long> {

    Api findByName(@Param("name") String name);

    @Query("MATCH (:APK)-[:APK_API_PACKAGE]->(api:API)<-[r:CONTAINS]-(:Tag) where api.name CONTAINS {name} RETURN api, r limit 1")
    Api findByNameLike(@Param("name") String name);

    @Query("MATCH (api:API)<-[r:APK_API_PACKAGE]-(:APK) RETURN DISTINCT api, COUNT(r) ORDER BY COUNT(r) DESC LIMIT 10")
    Collection<Api> getTop10Apis();

    @Query("MATCH (:APK)-[:APK_API_PACKAGE]->(api:API)<-[r:CONTAINS]-(:Tag) RETURN DISTINCT (api.name)")
    List<String> getAllApiName();


    Set<Api> findByIdIn(@Param("ids") List<Long> ids);

    Set<Api> findByNameIsIn(@Param("names") Set<String> names);

//    ApiPackage findByGenericName(@Param("genericName") String genericName);

//    Collection<ApiPackage> findByGenericNameLike(@Param("genericName") String genericName);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
