package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-07 15:13:30
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listMenu(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult listMenuDetail(Long id);

    ResponseResult editMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}
