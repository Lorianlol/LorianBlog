package com.lorian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lorian.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-15 20:02:11
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
