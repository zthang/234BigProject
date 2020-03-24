package com.tju.myproject.service;

import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ArticleService
{
    ArrayList<Domain>getDomains();
    ArrayList<Topic>getTopicsByDomainID(Integer domainID);
    Article addArticle(String title, Integer domainID, Integer topicID, String content, Integer authorID, String authorName, Date publishDate);
    Map<String, Object> getArticleByID(String aid,Integer userID);
    void deleteArticle(String aid);
    Map<String, Object> getArticleList(Integer domainID, Integer topicID, String kw, Integer page, Integer size);
    List<Article>searchArticleByContent(String searchStr,Integer page,Integer size);
    Map<String,Object>getRecommendArticlesAndPreTopics(Integer topicID);
    String favoriteArticle(Integer userID,String articleID);
}
