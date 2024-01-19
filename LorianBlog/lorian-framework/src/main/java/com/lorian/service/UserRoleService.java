package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 12:43:30
 */
public interface UserRoleService extends IService<UserRole> {
    List<Long> getRoleIdByUserId(Long userId);

    void deleteByUserId(Long userId);
}
