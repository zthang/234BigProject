package com.tju.myproject.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

@Data
public class BaseEntity
{
    @Id
    @GeneratedValue
    private Long id;
}
