package com.lorian.domain.vo;

import com.lorian.domain.entity.Role;
import com.lorian.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
* 修改用户时信息回显
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private User user;
}
