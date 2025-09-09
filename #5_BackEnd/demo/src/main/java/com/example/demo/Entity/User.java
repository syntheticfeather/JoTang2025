package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class User {
    Long id;

    @NotBlank
    String username;

    @NotBlank
    String password;

    String role;

    String phone;

    LocalDateTime createTime;

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
