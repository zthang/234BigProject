package com.tju.myproject.service;

import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public interface ArticleService
{
    ArrayList<Domain>getDomains();
    ArrayList<Topic>getTopicsByDomainID(Integer domainID);
    Article addArticle(String title,Integer domainID,Integer topicID,String content);
    Article getArcitleByID(String aid);
    void deleteArticle(String aid);
    List<Article> getArticleList(String topic, Integer page, Integer size);
    List<Article>searchArticleByContent(String searchStr,Integer page,Integer size);
}
