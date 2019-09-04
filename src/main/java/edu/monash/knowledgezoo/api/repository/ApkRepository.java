package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.Apk;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ApkRepository extends Neo4jRepository<Apk, Long> {

    Apk findBySha256(@Param("sha256") String sha256);

    Collection<Apk> findByNameLike(@Param("name") String name);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
