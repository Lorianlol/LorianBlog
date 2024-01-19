package com.lorian.controller;


import com.lorian.domain.dto.AddRoleDto;
import com.lorian.domain.dto.EditRoleDto;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.vo.EditRoleVo;
import com.lorian.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.listRole(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody EditRoleVo roleVo){
        return roleService.changeRoleStatus(roleVo);
    }

    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    //修改role信息时回显
    @GetMapping("/{id}")
    public ResponseResult showRole(@PathVariable("id") Long id){
        return roleService.showRole(id);
    }

//    修改role信息
    @PutMapping
    public ResponseResult editRole(@RequestBody EditRoleDto roleDto){
        return roleService.editRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        return roleService.deleteRole(id);
    }

    /*
    * 新增用户 查询所有状态正常角色
    * */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
