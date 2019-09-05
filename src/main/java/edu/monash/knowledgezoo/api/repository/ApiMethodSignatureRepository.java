package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.ApiMethodSignature;
import edu.monash.knowledgezoo.api.repository.entity.ApiPackage;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ApiMethodSignatureRepository extends Neo4jRepository<ApiMethodSignature, Long> {

    ApiMethodSignature findByName(@Param("name") String name);

    Collection<ApiMethodSignature> findByNameLike(@Param("name") String name);

//    ApiPackage findByGenericName(@Param("genericName") String genericName);

//    Collection<ApiPackage> findByGenericNameLike(@Param("genericName") String genericName);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
