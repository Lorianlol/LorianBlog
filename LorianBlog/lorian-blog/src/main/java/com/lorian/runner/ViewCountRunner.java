package com.lorian.runner;

import com.lorian.domain.entity.Article;
import com.lorian.mapper.ArticleMapper;
import com.lorian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        /*
        * 从数据库读取文章id和浏览量数据，整理成map形式
        * 存入redis中
        * */
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCount = articles.stream()
                .collect(Collectors.toMap(article->article.getId().toString(),
                        article -> {return article.getViewCount().intValue();}));

        redisCache.setCacheMap("article:viewCount", viewCount);//对用到redis中其实是hash类型
    }
}
