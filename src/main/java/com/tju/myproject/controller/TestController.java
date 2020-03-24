package com.tju.myproject.controller;

import com.tju.myproject.dao.ArticleRepository;
import com.tju.myproject.dao.TopicNodeRepository;
import com.tju.myproject.entity.Article;
import com.tju.myproject.entity.Topic;
import com.tju.myproject.entity.TopicNode;
import com.tju.myproject.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestController
{
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private Tools tools;
    @Autowired
    private TopicNodeRepository topicNodeRepository;
    @GetMapping("/api/test")
    public Object teststr(Integer str)
    {
        articleRepository.deleteAll();
        return null;
    }
    @GetMapping("/api/testa")
    public Object test(String str)throws Exception
    {
        File dic = new File("src/Articles_python_md");
        File[] tempList = dic.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            String content="";
            if (tempList[i].isFile())
            {
                String[] title=tempList[i].toString().split("\\.");
                if (title[1].equals("DS_Store"))
                    continue;
                LineIterator lineIterator = FileUtils.lineIterator(new File(tempList[i].toString()), "utf-8");
                while (lineIterator.hasNext())
                {
                    content += lineIterator.next();
                    content += "\n";
                }
                Article article=new Article();
                article.setDomain(3);
                article.setTopic(Integer.valueOf(title[0].split("/")[2]));
                article.setTitle(title[1]);
                article.setContent(content);
                article.setAuthorID(1);
                article.setAuthorName("zthang");
                article.setFavoriteNum(random(50L,200L).intValue());
                article.setPublishTime(randomDate("2018-01-01","2020-01-20"));
                article.setReadNum(random(500L,2000L).intValue());
                articleRepository.save(article);
                System.out.println(title[1]);
            }
        }
        return null;
    }
    public static Date randomDate(String beginDate, String endDate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            if (start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Long random(long begin, long end){
        long rtn = begin + (long)(Math.random()*(end - begin));
        if (rtn == begin || rtn == end){
            return random(begin, end);
        }
        return rtn;
    }
}
