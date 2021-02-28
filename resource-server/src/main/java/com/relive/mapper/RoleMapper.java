package com.relive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.relive.entity.Role;

import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:35
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getPermissionCodeByRoleCode(List<String> roleCode);

    List<Role> getRoleByUserId(Long userId);
}
