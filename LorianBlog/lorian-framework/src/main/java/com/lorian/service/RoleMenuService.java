package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-03-11 19:55:40
 */
public interface RoleMenuService extends IService<RoleMenu> {
    void deleteByRoleId(Long roleId);

    List<Long> getMenuIdByRoleId(Long roleId);
}
