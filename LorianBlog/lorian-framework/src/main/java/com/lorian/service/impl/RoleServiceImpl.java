package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.constants.SystemConstants;
import com.lorian.domain.dto.AddRoleDto;
import com.lorian.domain.dto.EditRoleDto;
import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.Role;
import com.lorian.domain.entity.RoleMenu;
import com.lorian.domain.vo.EditRoleVo;
import com.lorian.domain.vo.PageVo;
import com.lorian.domain.vo.RoleMenuTreeVo;
import com.lorian.mapper.RoleMapper;
import com.lorian.service.RoleMenuService;
import com.lorian.service.RoleService;
import com.lorian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-07 15:13:46
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //如果是管理员，则返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.like(StringUtils.hasText(status), Role::getStatus, status);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult changeRoleStatus(EditRoleVo roleVo) {
        Role role = getById(roleVo.getRoleId());
        role.setStatus(roleVo.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto roleVo) {
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        save(role);
        List<RoleMenu> roleMenus = roleVo.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showRole(Long id) {
        return ResponseResult.okResult(getById(id));
    }

    @Override
    public ResponseResult editRole(EditRoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.deleteByRoleId(role.getId());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = list(queryWrapper);
        return ResponseResult.okResult(roles);
    }

    public List<Role> getRoleList(){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = list(queryWrapper);
        return roles;
    }

}
