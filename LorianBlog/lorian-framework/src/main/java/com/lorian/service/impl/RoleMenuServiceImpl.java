package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.domain.entity.RoleMenu;
import com.lorian.mapper.RoleMenuMapper;
import com.lorian.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 19:55:40
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public void deleteByRoleId(Long roleId){
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        remove(queryWrapper);
    }

    @Override
    public List<Long> getMenuIdByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = list(queryWrapper);
        List<Long> menuIds = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        return menuIds;
    }
}
