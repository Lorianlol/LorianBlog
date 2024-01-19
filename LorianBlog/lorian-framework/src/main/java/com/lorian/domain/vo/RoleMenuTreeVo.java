package com.lorian.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuTreeVo {
    private List<NewRoleMenuVo> menus;

    private List<Long> checkedKeys;
}
