package com.lorian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lorian.domain.entity.Link;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-16 20:35:11
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
