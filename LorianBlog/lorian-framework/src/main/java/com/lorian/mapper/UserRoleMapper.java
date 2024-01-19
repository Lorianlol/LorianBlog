package com.lorian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lorian.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 12:43:23
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
