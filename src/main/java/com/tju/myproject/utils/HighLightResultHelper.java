package com.tju.myproject.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HighLightResultHelper implements SearchResultMapper
{
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        List<T> results = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            if (hit != null) {
                T result = null;
                if (StringUtils.hasText(hit.getSourceAsString()))
                {
                    result = JSONObject.parseObject(hit.getSourceAsString(), clazz);
                }
                // 高亮查询
                for (HighlightField field : hit.getHighlightFields().values()) {
                    try
                    {
                        PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
                    }
                    catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        System.out.println("设置高亮字段异常：{}"+e.getMessage());
                    }
                }
                results.add(result);
            }
        }
        return new AggregatedPageImpl<T>(results, pageable, response.getHits().getTotalHits(), response.getAggregations(), response.getScrollId());
    }


    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        for (HighlightField field : searchHit.getHighlightFields().values()) {
            T result = null;
            if (StringUtils.hasText(searchHit.getSourceAsString())) {
                result = JSONObject.parseObject(searchHit.getSourceAsString(), clazz);
            }
            try {
                PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.out.println("设置高亮字段异常：{}"+e.getMessage());
            }
            results.add(result);
        }
        return null;
    }

    private String concat(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return removeMarkdown(sb.toString());
    }

    private String removeMarkdown(String str)
    {
        String regEx="\\[.*?https*.*?\\)|\\\\{1,2}[a-z]*|!.*?[)]|[`|{}$#*+&‘”“’\\n]";
        Pattern p= Pattern.compile(regEx);
        Matcher m= p.matcher(str);
        return m.replaceAll("").trim();
    }
}