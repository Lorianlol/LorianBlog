package com.lorian.controller;

import com.lorian.domain.entity.LoginUser;
import com.lorian.domain.entity.Menu;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.User;
import com.lorian.domain.vo.AdminUserInfoVo;
import com.lorian.domain.vo.RoutersVo;
import com.lorian.domain.vo.UserInfoVo;
import com.lorian.enums.AppHttpCodeEnum;
import com.lorian.exception.SystemException;
import com.lorian.service.LoginService;
import com.lorian.service.MenuService;
import com.lorian.service.RoleService;
import com.lorian.utils.BeanCopyUtils;
import com.lorian.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
        //    提示 必须需要用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //据用户id获取权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //据用户id获取角色信息
        List<String> roleKeyList = roleService. selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        //    获取用户id
        Long userId = SecurityUtils.getUserId();
        //    查询menu 结果为tree形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //    封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
