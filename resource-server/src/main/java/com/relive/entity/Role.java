package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:30
 */
@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.INPUT)
    private Long id;

    private String roleName;

    private String roleCode;
}
