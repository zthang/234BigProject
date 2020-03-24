package com.tju.myproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NodeEntity(label = "Topic")
@Data
@NoArgsConstructor
public class TopicNode
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    public TopicNode(Long id, String name,String type)
    {
        this.id = id;
        this.name = name;
        this.type=type;
    }
}
