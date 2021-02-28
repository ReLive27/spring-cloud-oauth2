package com.relive.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.relive.entity.User;
import com.relive.entity.UserDetailsDTO;
import com.relive.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:18
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserDetailsDTO getUserByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        BeanUtils.copyProperties(user, userDetailsDTO);
        return userDetailsDTO;
    }
}
