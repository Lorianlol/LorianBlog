package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.dto.AddArticleDto;
import com.lorian.domain.entity.Article;
import com.lorian.domain.entity.ResponseResult;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Integer categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult listArticles(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult showArticleDetails(Long id);

    ResponseResult editArticle(AddArticleDto articleDto);

    ResponseResult deleteArticle(Long id);
}
