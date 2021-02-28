package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:28
 */
@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private String phone;

    private String email;
}
