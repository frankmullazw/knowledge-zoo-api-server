package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ReleaseTagRepository extends Neo4jRepository<ReleaseTag, Long> {

    ReleaseTag findByName(@Param("name") String name);

    Collection<ReleaseTag> findByNameLike(@Param("name") String name);

    // todo: Add release date interfaces

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
