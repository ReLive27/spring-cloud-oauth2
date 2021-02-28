package com.relive.oauth.service;

import com.relive.api.RoleServiceApi;
import com.relive.api.UserServiceApi;
import com.relive.entity.RoleDetailsDTO;
import com.relive.entity.UserDetailsDTO;
import com.relive.oauth.entity.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author ReLive
 * @Date 2021/1/17-15:25
 */
@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceApi userServiceApi;

    @Autowired
    private RoleServiceApi roleServiceApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username{}", username);

        UserDetailsDTO userDetailsDTO = userServiceApi.getUserByUsername(username);
        // 查询数据库操作
        if (userDetailsDTO == null) {
            throw new UsernameNotFoundException("user is not found");
        } else {
            List<RoleDetailsDTO> roleList = roleServiceApi.getRolesByUserId(userDetailsDTO.getId());
            Set<String> roles = roleList.stream().map(RoleDetailsDTO::getRoleCode).collect(Collectors.toSet());
            return new CustomUserDetails(userDetailsDTO.getId(), username, userDetailsDTO.getPassword(), roles);
        }
    }
}
