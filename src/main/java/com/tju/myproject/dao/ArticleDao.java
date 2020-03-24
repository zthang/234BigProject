package com.tju.myproject.dao;

import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface ArticleDao
{
    ArrayList<Domain>getDomains();
    ArrayList<Topic>getAllTopics();
    ArrayList<Topic>getTopicsByDomainID(@Param("domainID")Integer domainID);
    Map<String,Object>findUserFavoriteArticle(@Param("userID")Integer userID,@Param("articleID")String articleID);
    Integer insertFavorite(@Param("userID")Integer userID,@Param("articleID")String articleID);
    Integer setFavorite(@Param("userID")Integer userID,@Param("articleID")String articleID,@Param("favorite")Integer favorite);
}
