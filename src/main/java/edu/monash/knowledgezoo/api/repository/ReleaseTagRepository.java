package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface ReleaseTagRepository extends Neo4jRepository<ReleaseTag, Long> {

    ReleaseTag findByName(@Param("name") String name);

    Collection<ReleaseTag> findByNameLike(@Param("name") String name);

    @Query("MATCH (api:API{name: {0}})<-[:CONTAINS{state: \"INTRODUCE\"}]-(t:Tag) return t")
    ReleaseTag findIntroduceTagOfApiByName(String name);

    @Query("MATCH (api:API{name: {0}})<-[:CONTAINS{state: \"REMOVE\"}]-(t:Tag) return t")
    ReleaseTag findRemoveTagOfApiByName(String name);

    Collection<ReleaseTag> findByReleaseTagApiRelationships(@Param("name") String name);

    Set<ReleaseTag> findByNameIsIn(@Param("names") Set<String> names);


    // todo: Add release date interfaces

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
