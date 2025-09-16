package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Order {

    private Long id;

    @NotBlank
    private Long productId;
    @NotBlank
    private Long buyerId;

    @Pattern(regexp = "^(已下单|已取消|未支付)$")
    private String status; // 订单状态: 已下单/已取消/已完成等
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

// -- 订单表：记录用户下单信息
// CREATE TABLE `product_order` (
// `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
// `product_id` bigint NOT NULL COMMENT '商品ID',
// `buyer_id` bigint NOT NULL COMMENT '买家ID',
// `status` varchar(20) NOT NULL COMMENT '订单状态:已下单/已取消',
// `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
// `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
// PRIMARY KEY (`id`),
// KEY `idx_product` (`product_id`) COMMENT '商品索引',
// KEY `idx_buyer` (`buyer_id`) COMMENT '买家索引',
// CONSTRAINT `fk_order_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
// CONSTRAINT `fk_order_user` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
// ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单信息表';
