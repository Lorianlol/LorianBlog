package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.constants.SystemConstants;
import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.vo.MenuVo;
import com.lorian.domain.vo.NewRoleMenuVo;
import com.lorian.domain.vo.RoleMenuTreeVo;
import com.lorian.mapper.MenuMapper;
import com.lorian.service.MenuService;
import com.lorian.service.RoleMenuService;
import com.lorian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-07 15:13:30
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员 返回所有权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(userId == 1L){
        //    是管理员 则获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
        //    否则 获取当前用户所具有的Mapper
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = buildMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        queryWrapper.orderByAsc(Menu::getParentId);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listMenuDetail(Long id) {
        return ResponseResult.okResult(getById(id));
    }

    @Override
    public ResponseResult editMenu(Menu menu) {
        if(!menu.getParentId().equals(menu.getId())){
            updateById(menu);
            return ResponseResult.okResult();
        }else{
            throw new RuntimeException("修改菜单'写博文'失败，上级菜单不能选择自己");
        }
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, menuId);
        if(count(queryWrapper) > 0){
            throw new RuntimeException("存在子菜单不允许删除");
        }else{
            removeById(menuId);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult treeSelect() {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = menuMapper.selectAllRouterMenu();
        List<NewRoleMenuVo> newRoleMenuVos = BeanCopyUtils.copyBeanList(menus, NewRoleMenuVo.class);
        newRoleMenuVos = newRoleMenuVos.stream()
                .map(vo -> vo.setLabel(getById(vo.getId()).getMenuName()))
                .collect(Collectors.toList());
        List<NewRoleMenuVo> menuVoTree = buildMenuVoTree(newRoleMenuVos, 0L);

        return ResponseResult.okResult(menuVoTree);
    }

    /*
    * 修改角色信息时进行回显，并展示用户已有menu
    * */
    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = menuMapper.selectAllRouterMenu();
        List<NewRoleMenuVo> newRoleMenuVos = BeanCopyUtils.copyBeanList(menus, NewRoleMenuVo.class);
        newRoleMenuVos = newRoleMenuVos.stream()
                .map(vo -> vo.setLabel(getById(vo.getId()).getMenuName()))
                .collect(Collectors.toList());
        List<NewRoleMenuVo> menuVoTree = buildMenuVoTree(newRoleMenuVos, 0L);
        List<Long> menuIds = roleMenuService.getMenuIdByRoleId(id);

        return ResponseResult.okResult(new RoleMenuTreeVo(menuVoTree, menuIds));
    }

    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(buildMenuTree(menus, menu.getId())))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<NewRoleMenuVo> buildMenuVoTree(List<NewRoleMenuVo> menuVos, long parentId) {
        List<NewRoleMenuVo> menuVoTree = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(buildMenuVoTree(menuVos, menuVo.getId())))
                .collect(Collectors.toList());
        return menuVoTree;
    }
}
