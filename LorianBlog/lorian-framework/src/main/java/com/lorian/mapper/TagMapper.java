package com.lorian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lorian.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-06 20:23:32
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
