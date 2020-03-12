package com.tju.myproject.dao;

import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ArticleDao
{
    ArrayList<Domain>getDomains();
    ArrayList<Topic>getTopicsByDomainID(@Param("domainID")Integer domainID);
}
