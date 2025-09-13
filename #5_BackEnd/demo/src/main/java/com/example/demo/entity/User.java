package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {

    Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度必须在6-50之间")
    String password;

    @Pattern(regexp = "^(ADMIN|USER)$", message = "角色格式不正确")
    String role = "USER";

    @Pattern(regexp = "^1[34578]\\d{9}$", message = "手机号格式不正确")
    String phone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;
}
// CREATE TABLE `user` (
// `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
// `username` varchar(50) NOT NULL COMMENT '用户名',
// `password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
// `role` varchar(20) NOT NULL COMMENT '角色:ADMIN/USER',
// `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
// `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
// `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
// PRIMARY KEY (`id`),
// UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引'
// ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';
