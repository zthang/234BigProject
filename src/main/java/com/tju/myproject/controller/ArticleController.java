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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public ResultEntity addArticle(String title,Integer domainID,Integer topicID,String content)
    {
        Article article=new Article();
        article.setDomain(domainID);
        article.setTopic(topicID);
        article.setTitle(title);
        article.setContent(content);
        return new ResultEntity(0,null,articleService.addArticle(title,domainID,topicID,content));
    }
    @GetMapping("/article")
    public ResultEntity getArcitleByID(String aid)
    {
        return new ResultEntity(0,null,articleService.getArcitleByID(aid));
    }
    @GetMapping("/deleteArticle")
    public ResultEntity deleteArticle(String aid)
    {
        articleService.deleteArticle(aid);
        return new ResultEntity(0,"ok",null);
    }
    @GetMapping("/articleList")
    public ResultEntity getArticleList(String topic,Integer page,Integer size)
    {
        return new ResultEntity(0,null,articleService.getArticleList(topic,page,size));
    }
    @GetMapping("/searchArticle")
    public ResultEntity searchArticleByContent(String searchStr,Integer page,Integer size)
    {
        return new ResultEntity(0,null,articleService.searchArticleByContent(searchStr,page,size));
    }
}
