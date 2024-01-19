package com.lorian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lorian.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-08 14:05:53
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
