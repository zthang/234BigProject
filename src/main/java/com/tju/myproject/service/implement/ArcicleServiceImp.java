package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.tju.myproject.dao.ArticleDao;
import com.tju.myproject.dao.ArticleRepository;
import com.tju.myproject.dao.TopicNodeRepository;
import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.Topic;
import com.tju.myproject.entity.TopicNode;
import com.tju.myproject.service.ArticleService;
import com.tju.myproject.utils.HighLightResultHelper;
import com.tju.myproject.utils.Tools;
import org.elasticsearch.index.query.QueryBuilder;
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

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private TopicNodeRepository topicNodeRepository;
    @Override
    public ArrayList<Domain> getDomains()
    {
        return articleDao.getDomains();
    }

    @Override
    public ArrayList<Topic> getTopicsByDomainID(Integer domainID)
    {
        if (domainID==null)
            return articleDao.getAllTopics();
        else
            return articleDao.getTopicsByDomainID(domainID);
    }

    @Override
    public Article addArticle(String title, Integer domainID, Integer topicID, String content, Integer authorID, String authorName, Date publishDate)
    {
        Article article=new Article();
        article.setDomain(domainID);
        article.setTopic(topicID);
        article.setTitle(title);
        article.setContent(content);
        article.setFavoriteNum(0);
        article.setReadNum(0);
        article.setPublishTime(publishDate);
        article.setAuthorName(authorName);
        article.setAuthorID(authorID);
        return articleRepository.save(article);
    }

    @Override
    public Map<String, Object> getArticleByID(String aid, Integer userID)
    {
        Map<String,Object>tempMap=articleDao.findUserFavoriteArticle(userID,aid);
        Article temp=articleRepository.findById(aid).orElse(null);
        Map<String,Object>resMap=JSON.parseObject(JSON.toJSONString(temp), Map.class);
        temp.setReadNum(temp.getReadNum()+1);
        articleRepository.save(temp);
        if (tempMap==null||Integer.valueOf(tempMap.get("favorite").toString())==0)
            resMap.put("isFavor","data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/PjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+PHN2ZyB0PSIxNTg0ODg3MTMxMjc5IiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjQxMTciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCI+PGRlZnM+PHN0eWxlIHR5cGU9InRleHQvY3NzIj48L3N0eWxlPjwvZGVmcz48cGF0aCBkPSJNNTEyIDI5MS40Mmw0OS40OSAxMDAuMjhhMTA4Ljc2NyAxMDguNzY3IDAgMCAwIDgxLjkxIDU5LjVsMTEwLjY1IDE2LjA4LTgwLjA2IDc4LjA0Yy0yNS42NiAyNS0zNy4zNiA2MS4wMS0zMS4zIDk2LjMxbDE4LjkgMTEwLjIxLTk4Ljk2LTUyLjAzYy0xNS41NS04LjE4LTMzLjA2LTEyLjUtNTAuNjMtMTIuNXMtMzUuMDkgNC4zMi01MC42MyAxMi41bC05OC45NyA1Mi4wMyAxOC45LTExMC4yYzYuMDYtMzUuMzEtNS42NC03MS4zMS0zMS4yOS05Ni4zMWwtODAuMDctNzguMDVMMzgwLjYgNDUxLjJhMTA4Ljc1NyAxMDguNzU3IDAgMCAwIDgxLjkxLTU5LjUyTDUxMiAyOTEuNDJtMC0xMzMuMjNjLTExLjQxIDAtMjIuODIgNS45NS0yOC43IDE3Ljg0TDM5My42NSAzNTcuN2EzMi4wMDIgMzIuMDAyIDAgMCAxLTI0LjA5IDE3LjUxbC0yMDAuNDggMjkuMTNjLTI2LjI1IDMuODEtMzYuNzMgMzYuMDctMTcuNzQgNTQuNThMMjk2LjQgNjAwLjMzYTMyLjAwNSAzMi4wMDUgMCAwIDEgOS4yIDI4LjMybC0zNC4yNSAxOTkuNjdjLTMuNTUgMjAuNjggMTIuODkgMzcuNDggMzEuNTQgMzcuNDggNC45MiAwIDEwLTEuMTcgMTQuOS0zLjc1bDE3OS4zMi05NC4yN2M0LjY2LTIuNDUgOS43Ny0zLjY4IDE0Ljg5LTMuNjggNS4xMSAwIDEwLjIzIDEuMjMgMTQuODkgMy42OGwxNzkuMzIgOTQuMjdjNC45IDIuNTggOS45OCAzLjc1IDE0LjkgMy43NSAxOC42NCAwIDM1LjA4LTE2LjggMzEuNTMtMzcuNDhsLTM0LjI1LTE5OS42N2EzMS45OTYgMzEuOTk2IDAgMCAxIDkuMi0yOC4zMmwxNDUuMDctMTQxLjQxYzE4Ljk5LTE4LjUxIDguNTEtNTAuNzctMTcuNzMtNTQuNThsLTIwMC40OC0yOS4xM2EzMi4wMDIgMzIuMDAyIDAgMCAxLTI0LjA5LTE3LjUxTDU0MC43IDE3Ni4wM2MtNS44Ny0xMS44OS0xNy4yOC0xNy44NC0yOC43LTE3Ljg0eiIgZmlsbD0iI0YwODE0MCIgcC1pZD0iNDExOCI+PC9wYXRoPjwvc3ZnPg==");
        else if(Integer.valueOf(tempMap.get("favorite").toString())==1)
            resMap.put("isFavor","data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/PjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+PHN2ZyB0PSIxNTg0ODg3MTQwNDgxIiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjExNzAiIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCI+PGRlZnM+PHN0eWxlIHR5cGU9InRleHQvY3NzIj48L3N0eWxlPjwvZGVmcz48cGF0aCBkPSJNNTQwLjcgMTc2LjAzbDg5LjY2IDE4MS42N2EzMi4wMjUgMzIuMDI1IDAgMCAwIDI0LjA5IDE3LjUxbDIwMC40OCAyOS4xM2MyNi4yNSAzLjgxIDM2LjczIDM2LjA3IDE3LjczIDU0LjU4TDcyNy41OSA2MDAuMzNhMzEuOTk2IDMxLjk5NiAwIDAgMC05LjIgMjguMzJsMzQuMjUgMTk5LjY3YzQuNDggMjYuMTQtMjIuOTUgNDYuMDgtNDYuNDMgMzMuNzNsLTE3OS4zMi05NC4yN2MtOS4zMi00LjktMjAuNDYtNC45LTI5Ljc4IDBsLTE3OS4zMiA5NC4yN2MtMjMuNDggMTIuMzQtNTAuOTEtNy41OS00Ni40My0zMy43M2wzNC4yNS0xOTkuNjdhMzEuOTk2IDMxLjk5NiAwIDAgMC05LjItMjguMzJMMTUxLjM0IDQ1OC45MmMtMTguOTktMTguNTEtOC41MS01MC43NyAxNy43My01NC41OGwyMDAuNDgtMjkuMTNhMzEuOTc4IDMxLjk3OCAwIDAgMCAyNC4wOS0xNy41MWw4OS42Ni0xODEuNjdjMTEuNzQtMjMuNzggNDUuNjYtMjMuNzggNTcuNCAweiIgZmlsbD0iI0YwODE0MCIgcC1pZD0iMTE3MSI+PC9wYXRoPjwvc3ZnPg==");
        resMap.put("publishTime",new SimpleDateFormat("yyyy-MM-dd").format(new Date((Long)resMap.get("publishTime"))));
        List<Topic>topicList=articleDao.getAllTopics();
        for(Topic t:topicList)
        {
            if(t.getId().equals(Integer.valueOf(resMap.get("topic").toString())))
            {
                resMap.put("topicName", t.getTopic());
                break;
            }
        }
        return resMap;
    }

    @Override
    public void deleteArticle(String aid)
    {
        articleRepository.deleteById(aid);
    }

    @Override
    public Map<String, Object> getArticleList(Integer domainID, Integer topicID, String kw, Integer page, Integer size)
    {
        Pageable pageable = PageRequest.of(page, size);
        List<Article> pageArticle;
        HighlightBuilder.Field[] lst=new HighlightBuilder.Field[2];
        lst[0]=new HighlightBuilder.Field("content").preTags("<span style='color:red'>").postTags("</span>");
        lst[1]=new HighlightBuilder.Field("title").preTags("<span style='color:red'>").postTags("</span>");
        Map<String,Object>temp=new HashMap<>();
        AggregatedPage<Article>tempPages;
        Page<Article>tempP;
        if (domainID!=null)
        {
            if(kw!=null&&kw.length()>0)
            {
                NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(page,size))
                .withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("domain",domainID))
                .must(QueryBuilders.multiMatchQuery(kw,"title","content")))
                .withHighlightFields(lst);
                tempPages=elasticsearchTemplate.queryForPage(queryBuilder.build(), Article.class, new HighLightResultHelper());
                pageArticle=tempPages.getContent();
                temp.put("totalPagesNum",tempPages.getTotalPages());
            }
            else
            {
                tempP=articleRepository.findAllByDomain(pageable,domainID);
                pageArticle=tempP.getContent();
                temp.put("totalPagesNum",tempP.getTotalPages());
            }
        }
        else
        {
            if(topicID!=null)
                if (kw!=null&&kw.length()>0)
                {
                    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(page,size))
                    .withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("topic",topicID))
                    .must(QueryBuilders.multiMatchQuery(kw,"title","content")))
                    .withHighlightFields(lst);
                    tempPages=elasticsearchTemplate.queryForPage(queryBuilder.build(), Article.class, new HighLightResultHelper());
                    pageArticle=tempPages.getContent();
                    temp.put("totalPagesNum",tempPages.getTotalPages());
                }
                else
                {
                    tempP=articleRepository.findAllByTopic(pageable,topicID);
                    pageArticle=tempP.getContent();
                    temp.put("totalPagesNum",tempP.getTotalPages());
                }
            else
                if(kw!=null&&kw.length()>0)
                {
                    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(page,size))
                    .withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.multiMatchQuery(kw,"title","content")))
                    .withHighlightFields(lst);
                    tempPages=elasticsearchTemplate.queryForPage(queryBuilder.build(), Article.class, new HighLightResultHelper());
                    pageArticle=tempPages.getContent();
                    temp.put("totalPagesNum",tempPages.getTotalPages());
                }
                else
                {
                    tempP=articleRepository.findAll(pageable);
                    pageArticle=tempP.getContent();
                    temp.put("totalPagesNum",tempP.getTotalPages());
                }
        }
        List<Article> articles=pageArticle;
        for(Article a:articles)
        {
            if(a.getContent()!=null&&a.getContent().length()>0)
            {
                String tempStr=tools.removeMarkdown(a.getContent());
                a.setContent(tempStr.substring(0,Math.min(tempStr.length(),500)));
            }

        }
        temp.put("articles",articles);
        return temp;
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

    @Override
    public Map<String,Object> getRecommendArticlesAndPreTopics(Integer topicID)
    {
        Map<String,Object>resMap=new HashMap<>();
        List<Topic>preTopics=new ArrayList<>();
        Collection<TopicNode>preNodes=topicNodeRepository.findPreNodes(topicID);
        for(TopicNode topicNode:preNodes)
        {
            if(topicNode.getType().equals("topic"))
            {
                Topic topic=new Topic();
                topic.setId(topicNode.getId().intValue());
                topic.setTopic(topicNode.getName());
                preTopics.add(topic);
            }
            else if(topicNode.getType().equals("domain"))
            {
                Collection<TopicNode>topics=topicNodeRepository.findSubNodes(topicNode.getId().intValue());
                for(TopicNode temp:topics)
                {
                    Topic topic=new Topic();
                    topic.setId(temp.getId().intValue());
                    topic.setTopic(temp.getName());
                    preTopics.add(topic);
                }
            }
        }
        List<Article>articles=articleRepository.findAllByTopic(PageRequest.of(0, 100),topicID).getContent();
        List<Article> tempList=new ArrayList<>();
        tempList.addAll(articles);
        Collections.sort(tempList);
        tempList=tempList.subList(0,Math.min(3,tempList.size()));
        for(Article a:tempList)
        {
            String temp=tools.removeMarkdown(a.getContent());
            a.setContent(temp.substring(0,Math.min(temp.length(),500)));
        }
        resMap.put("recommendArticles",tempList);
        resMap.put("preTopics",preTopics);
        return resMap;
    }

    @Override
    public String favoriteArticle(Integer userID, String articleID)
    {
        Map<String,Object>tempMap=articleDao.findUserFavoriteArticle(userID,articleID);
        if (tempMap==null||tempMap.size()==0)
        {
            Article temp=articleRepository.findById(articleID).orElse(null);
            temp.setFavoriteNum(temp.getFavoriteNum()+1);
            articleRepository.save(temp);
            return articleDao.insertFavorite(userID,articleID)==1?"插入用户收藏数据成功":"插入用户收藏数据失败";
        }
        else if(Integer.valueOf(tempMap.get("favorite").toString())==0)
        {
            Article temp=articleRepository.findById(articleID).orElse(null);
            temp.setFavoriteNum(temp.getFavoriteNum()+1);
            articleRepository.save(temp);
            return articleDao.setFavorite(userID,articleID,1)==1?"操作0->1成功":"操作0->1失败";
        }
        else
        {
            Article temp=articleRepository.findById(articleID).orElse(null);
            temp.setFavoriteNum(temp.getFavoriteNum()-1);
            articleRepository.save(temp);
            return articleDao.setFavorite(userID,articleID,0)==1?"操作1->0成功":"操作1->0失败";
        }
    }
}
