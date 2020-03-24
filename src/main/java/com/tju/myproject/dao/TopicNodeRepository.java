package com.tju.myproject.dao;

import com.tju.myproject.entity.TopicNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TopicNodeRepository extends Neo4jRepository<TopicNode, Long>
{
    @Query("match (m)-[r:DEPEND_ON]->(n) where m.id=$nodeID and m.type='topic' return n")
    Collection<TopicNode> findPreNodes(@Param("nodeID") Integer nodeID);
    @Query("match (m)-[r:IS_PART_OF]->(n) where n.id=$nodeID return m")
    Collection<TopicNode>findSubNodes(@Param("nodeID")Integer nodeID);
}
