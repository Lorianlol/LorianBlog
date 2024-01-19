package com.lorian.filter;

import com.alibaba.fastjson.JSON;
import com.lorian.domain.entity.LoginUser;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.enums.AppHttpCodeEnum;
import com.lorian.utils.JwtUtil;
import com.lorian.utils.RedisCache;
import com.lorian.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    /*
    * 获取token
    * 解析token获取uerId
    * 从redis中拿到用户信息
    * 存入SecurityContextHolder
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //    获取请求头中的token
        String token = request.getHeader("token");
    //    说明该接口不需要登录
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }
    //    解析token拿到userId
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token 超时/token非法
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();

        //    从redis中拿到个人信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);

        if(Objects.isNull(loginUser)){
        //    获取不到 登陆过期 提示重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
