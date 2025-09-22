-- 确保使用正确的数据库
USE JoTang2025;

-- 用户表：存储用户信息及权限角色（将表名从 user 改为 users，避免关键字冲突）
CREATE TABLE IF NOT EXISTS `user` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
`username` varchar(50) NOT NULL COMMENT '用户名',
`password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
`role` varchar(20) NOT NULL COMMENT '角色:ADMIN/USER',
`phone` varchar(20) DEFAULT NULL COMMENT '手机号',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 商品表：存储二手商品/需求信息
CREATE TABLE IF NOT EXISTS `product` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
`title` varchar(100) NOT NULL COMMENT '商品标题',
`description` text COMMENT '商品描述',
`type` varchar(20) NOT NULL COMMENT '类型:二手物品/代取需求',
`price` decimal(10,2) NOT NULL COMMENT '价格',
`status` varchar(20) NOT NULL COMMENT '状态:未售出/已售出',
`publisher_id` bigint NOT NULL COMMENT '发布者ID',
`publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`),
KEY `idx_publisher` (`publisher_id`) COMMENT '发布者索引',
KEY `idx_status_type` (`status`,`type`) COMMENT '状态类型联合索引',
CONSTRAINT `fk_product_user` FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品信息表';

-- 订单表：记录用户下单信息
CREATE TABLE IF NOT EXISTS `product_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
`product_id` bigint NOT NULL COMMENT '商品ID',
`buyer_id` bigint NOT NULL COMMENT '买家ID',
`status` varchar(20) NOT NULL COMMENT '订单状态:已下单/已取消/未支付',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`),
KEY `idx_product` (`product_id`) COMMENT '商品索引',
KEY `idx_buyer` (`buyer_id`) COMMENT '买家索引',
CONSTRAINT `fk_order_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
CONSTRAINT `fk_order_user` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单信息表';

-- 插入测试数据
INSERT IGNORE INTO `users` (id, username, password, role) VALUES 
(1, 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN'),
(2, 'user1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER'),
(3, 'user2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER'),
(4, 'user3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER');

INSERT IGNORE INTO `product` (id, title, description, type, price, status, publisher_id) VALUES 
(1, 'iPhone X', 'Apple iPhone X 64GB 深空灰色 移动联通电信4G手机', '二手物品', 9999.00, '未售出', 1),
(2, 'MacBook Pro', 'Apple MacBook Pro 13.3英寸 256GB SSD 银色 笔记本电脑', '二手物品', 12999.00, '未售出', 1),
(3, '需求：小米路由器', '小米路由器 3代 全网通 双频 双天线 千兆双核 4G智能路由器', '代取需求', 399.00, '未售出', 2),
(4, '需求：小米手机', '小米手机 6 64GB 钻雕金 移动联通电信4G手机', '代取需求', 1999.00, '未售出', 3);

INSERT IGNORE INTO `product_order` (id, product_id, buyer_id, status) VALUES 
(1, 1, 2, '已下单'),
(2, 2, 3, '已下单'),
(3, 3, 4, '已下单'),
(4, 4, 1, '已下单');