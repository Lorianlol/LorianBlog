package com.lorian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lorian.domain.dto.UserDto;
import com.lorian.domain.entity.ResponseResult;
import com.lorian.domain.entity.Role;
import com.lorian.domain.entity.User;
import com.lorian.domain.entity.UserRole;
import com.lorian.domain.vo.EditUserVo;
import com.lorian.domain.vo.PageVo;
import com.lorian.domain.vo.UserInfoVo;
import com.lorian.enums.AppHttpCodeEnum;
import com.lorian.exception.SystemException;
import com.lorian.mapper.UserMapper;
import com.lorian.service.RoleService;
import com.lorian.service.UserRoleService;
import com.lorian.service.UserService;
import com.lorian.utils.BeanCopyUtils;
import com.lorian.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-20 17:16:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取uerId
        Long userId = SecurityUtils.getUserId();
        //通过uerId获取用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //检查用户名 昵称 密码 邮箱是否为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //检查用户名 昵称 邮箱是否已存在
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //密码加密
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        queryWrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addUser(UserDto userDto) {
        //用户名不能为空，否则提示：必需填写用户名
        if(!StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //用户名必须之前未存在，否则提示：用户名已存在
        if(userNameExist(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //手机号必须之前未存在，否则提示：手机号已存在
        if(phonenumberExist(userDto.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //邮箱必须之前未存在，否则提示：邮箱已存在
        if(emailExist(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //注意：新增用户时注意密码加密存储。
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        save(user);

        List<UserRole> userRoles = userDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult deleteUser(Long id) {
        Long userId = SecurityUtils.getUserId();
        if(userId.equals(id)){
            throw new RuntimeException("不能删除当前操作的用户");
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showUserInfo(Long id) {
        User user = getById(id);
        List<Role> roleList = roleService.getRoleList();
        List<Long> roleIds = userRoleService.getRoleIdByUserId(id);
        return ResponseResult.okResult(new EditUserVo(roleIds, roleList, user));
    }

    @Override
    public ResponseResult editUser(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        updateById(user);

        userRoleService.deleteByUserId(user.getId());
        List<UserRole> userRoles = userDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    private boolean phonenumberExist(String phonenumber){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }
}
