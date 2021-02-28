package com.relive.api;

import com.relive.entity.RoleDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:42
 */
@Component
@FeignClient(value = "RESOURCE-SERVER")
@RequestMapping("/resource")
public interface RoleServiceApi {
    @GetMapping("/roles/{id}")
    List<RoleDetailsDTO> getRolesByUserId(@PathVariable(value = "id") Long id);
}
