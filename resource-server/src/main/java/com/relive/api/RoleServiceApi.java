package com.relive.api;

import com.relive.entity.RoleDetailsDTO;
import com.relive.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:40
 */
@RestController
public class RoleServiceApi {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles/{id}")
    public List<RoleDetailsDTO> getRolesByUserId(@PathVariable Long id) {
        return roleService.getRoleByUserId(id);
    }
}
