package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.ApiPackage;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ApiPackageRepository extends Neo4jRepository<ApiPackage, Long> {

    ApiPackage findByName(@Param("name") String name);

    Collection<ApiPackage> findByNameLike(@Param("name") String name);

    Collection<ApiPackage> findByLibrary(@Param("isLibrary") boolean isLibrary);

//    ApiPackage findByGenericName(@Param("genericName") String genericName);

//    Collection<ApiPackage> findByGenericNameLike(@Param("genericName") String genericName);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}