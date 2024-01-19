package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.Category;
import com.lorian.domain.entity.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-02-15 20:02:13
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(Category category);

    ResponseResult showCategoryDetail(Long id);

    ResponseResult editCategory(Category category);

    ResponseResult deleteCategory(Long id);
}
