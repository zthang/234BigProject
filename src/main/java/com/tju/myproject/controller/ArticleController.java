package com.tju.myproject.controller;

import com.tju.myproject.dao.ArticleRepository;
import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.entity.Topic;
import com.tju.myproject.service.ArticleService;
import com.tju.myproject.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ArticleController
{
    @Autowired
    private ArticleService articleService;
    @GetMapping("/domains")
    public ResultEntity getDomains()
    {
        ArrayList<Domain>arrayList=articleService.getDomains();
        if(arrayList.size()>0)
            return new ResultEntity(0,null,arrayList);
        else
            return new ResultEntity(401,"数据库错误!",null);
    }
    @GetMapping("/topics")
    public ResultEntity getTopicsByDomainID(Integer domain)
    {
        ArrayList<Topic>arrayList=articleService.getTopicsByDomainID(domain);
        if(arrayList.size()>0)
            return new ResultEntity(0,null,arrayList);
        else
            return new ResultEntity(401,"数据库错误!",null);
    }
    @PostMapping("/addArticle")
    public ResultEntity addArticle(String title, Integer domainID, Integer topicID, String content, Integer authorID, String authorName, String publishDate)throws Exception
    {
        Article article=new Article();
        article.setDomain(domainID);
        article.setTopic(topicID);
        article.setTitle(title);
        article.setContent(content);
        return new ResultEntity(0,null,articleService.addArticle(title,domainID,topicID,content,authorID,authorName,new SimpleDateFormat("yyyy-MM-dd").parse(publishDate)));
    }
    @GetMapping("/article")
    public ResultEntity getArcitleByID(String aid,Integer userID)
    {
        return new ResultEntity(0,null,articleService.getArticleByID(aid,userID));
    }
    @GetMapping("/deleteArticle")
    public ResultEntity deleteArticle(String aid)
    {
        articleService.deleteArticle(aid);
        return new ResultEntity(0,"ok",null);
    }
    @GetMapping("/articleList")
    public ResultEntity getArticleList(Integer domainID,Integer topicID,String kw,Integer page, Integer size)
    {
        return new ResultEntity(0,null,articleService.getArticleList(domainID,topicID,kw,page,size));
    }
    @GetMapping("/searchArticle")
    public ResultEntity searchArticleByContent(String searchStr,Integer page,Integer size)
    {
        return new ResultEntity(0,null,articleService.searchArticleByContent(searchStr,page,size));
    }
    @GetMapping("/getPreTopics")
    public ResultEntity getPreTopics(Integer topicID)
    {
        return new ResultEntity(0,null,articleService.getRecommendArticlesAndPreTopics(topicID));
    }
    @GetMapping("/favorArticle")
    public ResultEntity favorArticle(Integer userID,String articleID)
    {
        return new ResultEntity(0,articleService.favoriteArticle(userID,articleID),null);
    }
}
