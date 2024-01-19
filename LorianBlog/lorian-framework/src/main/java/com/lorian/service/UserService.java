package com.lorian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lorian.domain.dto.UserDto;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-20 17:16:03
 */
public interface UserService extends IService<User> {


    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserDto userDto);

    ResponseResult deleteUser(Long id);

    ResponseResult showUserInfo(Long id);

    ResponseResult editUser(UserDto userDto);
}
