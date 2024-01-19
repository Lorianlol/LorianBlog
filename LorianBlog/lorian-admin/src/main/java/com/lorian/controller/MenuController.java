package com.lorian.controller;

import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listMenu(String status, String menuName){
        return menuService.listMenu(status, menuName);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("{id}")
    public ResponseResult listMenuDetail(@PathVariable("id") Long id){
        return menuService.listMenuDetail(id);
    }

    @PutMapping()
    public ResponseResult editMenu(@RequestBody Menu menu){
        return menuService.editMenu(menu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }

    @GetMapping("roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }
}
