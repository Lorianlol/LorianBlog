package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.constants.SystemConstants;
import com.lorian.domain.entity.Article;
import com.lorian.domain.entity.Category;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.vo.CategoryVo;
import com.lorian.domain.vo.PageVo;
import com.lorian.mapper.CategoryMapper;
import com.lorian.service.ArticleService;
import com.lorian.service.CategoryService;
import com.lorian.utils.BeanCopyUtils;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-02-15 20:02:14
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
    //    查询文章列表(正常状态)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(queryWrapper);
        //    获取categoryId + 去重
        Set<Long> categoyIds = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //    查询分类表 + 过滤正常状态
        List<Category> categories = listByIds(categoyIds);
       categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //    封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);


    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVo = BeanCopyUtils.copyBeanList(list, CategoryVo.class);

        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        queryWrapper.eq(StringUtils.hasText(status), Category::getStatus, status);

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showCategoryDetail(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult editCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
