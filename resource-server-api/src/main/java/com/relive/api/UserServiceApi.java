package com.relive.api;

import com.relive.entity.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:14
 */
@Component
@FeignClient(value = "RESOURCE-SERVER")
@RequestMapping("/resource")
public interface UserServiceApi {

    @GetMapping("/user/{username}")
    UserDetailsDTO getUserByUsername(@PathVariable(value = "username") String username);
}
