package com.lorian.job;

import com.lorian.domain.entity.Article;
import com.lorian.service.ArticleService;
import com.lorian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    /*
    * 需要对所有文章的浏览量进行更新
    * 借助mybatisplus中实现的批量操作方法
    * */
    @Autowired
    private ArticleService articleService;
    /*
    * 定义定时任务
    * 定时将redis数据写回mysql
    * */
    @Scheduled(cron = "1/20 * * * * ?")//加上定时注解 每隔5s执行一次
    public void updateViewCount(){

        //    获取redis中得浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");


        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //双列集合是不能直接转换成流对象的，使用entrySet/keySet转换为单列集合后.stream()
        //    更新到数据库中

        articleService.updateBatchById(articles);
    }
}
