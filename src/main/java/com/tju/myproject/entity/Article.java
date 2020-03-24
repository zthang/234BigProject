package com.tju.myproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document(indexName = "bigproject",type = "article")
public class Article implements Comparable<Article>
{
    @Id
    private String id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Integer)
    private Integer domain;
    @Field(type = FieldType.Integer)
    private Integer topic;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishTime;
    @Field(type = FieldType.Integer)
    private Integer authorID;
    @Field(type = FieldType.Text)
    private String authorName;
    @Field(type = FieldType.Integer)
    private Integer readNum;
    @Field(type = FieldType.Integer)
    private Integer favoriteNum;

    @Override
    public int compareTo(Article o)
    {
        return (float)this.getFavoriteNum()/this.getReadNum()-(float)o.getFavoriteNum()/o.getReadNum()>0?-1:1;
    }
}
