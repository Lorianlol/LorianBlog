package com.lorian.service.impl;

import com.lorian.domain.entity.LoginUser;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.User;
import com.lorian.domain.vo.BlogUserLoginVo;
import com.lorian.domain.vo.UserInfoVo;
import com.lorian.service.BlogLoginService;
import com.lorian.utils.BeanCopyUtils;
import com.lorian.utils.JwtUtil;
import com.lorian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //默认从内存中认证，因此自己实现接口，从数据库中获取。 //UserDetailsService
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //如果查到数据，则生成jwt，存入redis中
        if(Objects.isNull(authentication)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        redisCache.setCacheObject("bloglogin:"+userId, loginUser);

        //将token和userinfo封装 返回
        //将User换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析uerId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long id = loginUser.getUser().getId();
        //删除redis中用户信息
        redisCache.deleteObject("bloglogin:"+id);
        return ResponseResult.okResult();
    }
}
