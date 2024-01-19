package com.lorian.service.impl;

import com.lorian.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("ps")
public class PermissionService {

    public boolean hasPermission(String permission){
    //    如果是超级管理员 直接返回true
        if(SecurityUtils.isAdmin()) return true;
    //    否则检查权限列表中是否含有permission
        return SecurityUtils.getLoginUser().getPermissions().contains(permission);
    }
}
