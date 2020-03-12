package com.tju.myproject.dao;

import com.tju.myproject.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<Article,String>
{
    Page<Article> findAllByTopic(Pageable pageable,Integer topic);
}
