package com.relive.service;

import com.relive.entity.Role;
import com.relive.entity.RoleDetailsDTO;
import com.relive.mapper.RoleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:37
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<String> getPermissionCodeByRoleCode(List<String> roleCode) {
        return roleMapper.getPermissionCodeByRoleCode(roleCode);
    }

    public List<RoleDetailsDTO> getRoleByUserId(Long userId) {
        List<Role> role = roleMapper.getRoleByUserId(userId);
        List<RoleDetailsDTO> roleDetailsDTOS = role.stream().map(data -> {
            RoleDetailsDTO roleDetailsDTO = new RoleDetailsDTO();
            BeanUtils.copyProperties(data, roleDetailsDTO);
            return roleDetailsDTO;
        }).collect(Collectors.toList());

        return roleDetailsDTOS;
    }
}
