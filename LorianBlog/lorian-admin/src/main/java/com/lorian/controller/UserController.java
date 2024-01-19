package com.lorian.controller;

import com.lorian.domain.dto.UserDto;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){
        return userService.listUser(pageNum, pageSize, userName, phonenumber, status);
    }

    @PostMapping()
    public ResponseResult addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

    /*
    * 修改用户信息时进行用户信息回显
    * */
    @GetMapping("/{id}")
    public ResponseResult showUserInfo(@PathVariable("id") Long id){
        return userService.showUserInfo(id);
    }

    @PutMapping
    public ResponseResult editUser(@RequestBody UserDto userDto){
        return userService.editUser(userDto);
    }
}
