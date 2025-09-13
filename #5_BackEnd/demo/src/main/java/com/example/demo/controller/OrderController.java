package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ApiResponse;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> addOrder(@RequestBody Order order, HttpServletRequest request) {
        // 从请求属性中获取用户ID (由拦截器放入)
        Long buyerId = (Long) request.getAttribute("userId");
        order.setBuyerId(buyerId);

        Order createdOrder = orderService.addOrder(order);
        return ResponseEntity.ok(ApiResponse.success(createdOrder, "订单创建成功"));
    }

    // 取消订单
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@RequestParam Long orderId, HttpServletRequest request) {
        // 从请求属性中获取用户ID
        Long buyerId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        Order canceledOrder = orderService.cancelOrder(orderId, buyerId, role);
        return ResponseEntity.ok(ApiResponse.success(canceledOrder, "订单取消成功"));
    }

    // 获取订单详情
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    // 获取用户订单列表
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders(HttpServletRequest request) {
        // 从请求属性中获取用户ID
        Long buyerId = (Long) request.getAttribute("userId");

        List<Order> orders = orderService.getOrdersByBuyerId(buyerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    // 删除订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId, HttpServletRequest request) {
        // 从请求属性中获取用户ID
        Long buyerId = (Long) request.getAttribute("userId");

        boolean deleted = orderService.deleteOrder(orderId, buyerId);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "订单删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.error(500, "订单删除失败"));
        }
    }
}
