package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.dto.AddRoleDto;
import com.lorian.domain.dto.EditRoleDto;
import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.Role;
import com.lorian.domain.vo.EditRoleVo;
import com.lorian.domain.vo.RoleMenuTreeVo;
import org.apache.commons.math3.analysis.function.Add;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-07 15:13:46
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeRoleStatus(EditRoleVo roleVo);

    ResponseResult addRole(AddRoleDto roleVo);

    ResponseResult showRole(Long id);

    ResponseResult editRole(EditRoleDto roleDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();

    /*
    * 获得状态正常role列表
    * */
    List<Role> getRoleList();
}
