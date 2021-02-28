package com.relive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:31
 */
@Data
@TableName("Permission")
public class Permission {
    @TableId(type = IdType.INPUT)
    private Long id;

    private String permissionName;

    private String permissionCode;

    private String permissionType;
}
