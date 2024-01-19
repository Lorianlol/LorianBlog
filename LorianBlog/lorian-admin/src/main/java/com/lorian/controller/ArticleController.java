package com.lorian.controller;

import com.lorian.domain.dto.AddArticleDto;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping()
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult listArticles(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.listArticles(pageNum, pageSize, title, summary);
    }

    /*
    * 更新文章时进行回显
    * */
    @GetMapping("/{id}")
    public ResponseResult showArticleDetails(@PathVariable("id") Long id){
        return articleService.showArticleDetails(id);
    }

    @PutMapping()
    public ResponseResult editArticle(@RequestBody AddArticleDto articleDto){
        return articleService.editArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
