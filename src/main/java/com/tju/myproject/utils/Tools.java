package com.tju.myproject.utils;

import org.springframework.stereotype.Repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class Tools
{
    public String removeMarkdown(String str)
    {
        if(str!=null&&str.length()>0)
        {
            String regEx="<svg.*?/svg>|<br.*?>|<script.*?/script>|\\[.*?https*.*?\\)|\\\\{1,2}[a-z]*|!.*?[)]|[`|{}$#*+&‘”“’\\n]";
            Pattern p= Pattern.compile(regEx);
            Matcher m= p.matcher(str);
            return m.replaceAll("").trim();
        }
        else
            return str;
    }
}
