package com.tju.myproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "DEPEND_ON")
@Data
@NoArgsConstructor

public class DependRelation
{
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private TopicNode startNode;
    @EndNode
    private TopicNode endNode;
    public DependRelation(Long id, TopicNode startTopic, TopicNode endTopic) {
        this.id = id;
        this.startNode = startTopic;
        this.endNode = endTopic;
    }
}
