package com.relive.api;

import com.relive.entity.UserDetailsDTO;
import com.relive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:16
 */
@RestController
public class UserServiceApi {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{username}")
    public UserDetailsDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
