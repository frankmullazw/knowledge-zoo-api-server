package edu.monash.knowledgezoo.api.repository;

import edu.monash.knowledgezoo.api.repository.entity.Permission;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PermissionRepository extends Neo4jRepository<Permission, Long> {

    Permission findByName(@Param("name") String name);

    Collection<Permission> findByNameLike(@Param("name") String name);

    Permission findByGenericName(@Param("genericName") String genericName);

    Collection<Permission> findByGenericNameLike(@Param("genericName") String genericName);

    @Query("MATCH (p:Permission)<-[r:DECLARES]-(:APK) RETURN DISTINCT p, COUNT(r) ORDER BY COUNT(r) DESC LIMIT 10")
    Collection<Permission> getTop10Permissions();

    @Query("MATCH (p:Permission) where toLower(p.genericName) CONTAINS toLower({genericName}) return p limit 1")
    Permission findFirstByGenericName(@Param("genericName") String genericName);

//    @Query("MATCH (m:Apk)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT {limit}")
//    Collection<Apk> graph(@Param("limit") int limit);

}
