package com.relive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ReLive
 * @Date 2021/3/15-15:22
 */
@RestController
public class LoginController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/remove")
    public void remove(@RequestHeader("Authorization") String token){
        consumerTokenServices.revokeToken(token.substring("Bearer".length()).trim());
    }
}
