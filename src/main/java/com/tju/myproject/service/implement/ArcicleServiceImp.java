package com.tju.myproject.service.implement;

import com.tju.myproject.dao.ArticleDao;
import com.tju.myproject.dao.ArticleRepository;
import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;
import com.tju.myproject.service.ArticleService;
import com.tju.myproject.utils.HighLightResultHelper;
import com.tju.myproject.utils.Tools;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "ArticleService")
public class ArcicleServiceImp implements ArticleService
{
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private Tools tools;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Override
    public ArrayList<Domain> getDomains()
    {
        return articleDao.getDomains();
    }

    @Override
    public ArrayList<Topic> getTopicsByDomainID(Integer domainID)
    {
        return articleDao.getTopicsByDomainID(domainID);
    }

    @Override
    public Article addArticle(String title, Integer domainID, Integer topicID, String content)
    {
        Article article=new Article();
        article.setDomain(domainID);
        article.setTopic(topicID);
        article.setTitle(title);
        article.setContent(content);
        return articleRepository.save(article);
    }

    @Override
    public Article getArcitleByID(String aid)
    {
        return articleRepository.findById(aid).orElse(null);
    }

    @Override
    public void deleteArticle(String aid)
    {
        articleRepository.deleteById(aid);
    }

    @Override
    public List<Article> getArticleList(String topic, Integer page, Integer size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> pageArticle;
        if (topic.equals("all"))
        {
            pageArticle=articleRepository.findAll(pageable);
        }
        else
        {
            Integer topicID=Integer.valueOf(topic);
            pageArticle=articleRepository.findAllByTopic(pageable,topicID);
        }
        List<Article> articles=pageArticle.getContent();
        for(Article a:articles)
        {
            String temp=tools.removeMarkdown(a.getContent());
            a.setContent(temp.substring(0,Math.min(temp.length(),500)));
        }
        return articles;
    }

    @Override
    public List<Article> searchArticleByContent(String searchStr,Integer page,Integer size)
    {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
        .withPageable(PageRequest.of(page,size))
        .withQuery(QueryBuilders.multiMatchQuery(searchStr,"title","content"))
        .withHighlightFields(new HighlightBuilder.Field("content").preTags("<span style='color:#dd4b39'>").postTags("</span>"));
        AggregatedPage<Article> articles = elasticsearchTemplate.queryForPage(queryBuilder.build(), Article.class, new HighLightResultHelper());
        return articles.getContent();
    }
}
