package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lorian.constants.SystemConstants;
import com.lorian.domain.entity.LoginUser;
import com.lorian.domain.entity.User;
import com.lorian.mapper.MenuMapper;
import com.lorian.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户 没有查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        //返回用户信息
        //TODO 查询权限信息封装
        //如果 用户是后台管理员，则查询权限
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());
            return  new LoginUser(user, perms);
        }
        return new LoginUser(user, null);
    }
}
