package com.lorian.service;


import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
