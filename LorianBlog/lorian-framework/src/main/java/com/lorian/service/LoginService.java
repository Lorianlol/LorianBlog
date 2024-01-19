package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
