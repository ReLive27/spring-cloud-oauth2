package com.relive.controller;

import com.relive.oauth.conf.CurrentUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author ReLive
 * @Date 2021/1/17-16:48
 */
@RestController
public class OrderController {

    @GetMapping(value = "get")
    @PreAuthorize("hasAuthority('home-page')")
//    @Secured({"admin","super"})
    public Object get(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getCredentials();
//        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();


        CurrentUser userDTO = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "success";
    }

}
