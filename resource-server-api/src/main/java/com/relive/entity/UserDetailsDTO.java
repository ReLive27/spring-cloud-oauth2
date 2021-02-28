package com.relive.entity;

import lombok.Data;

/**
 * @Author ReLive
 * @Date 2021/2/27-16:22
 */
@Data
public class UserDetailsDTO {

    private Long id;

    private String username;

    private String password;

    private String nickName;

    private String phone;

    private String email;

}
