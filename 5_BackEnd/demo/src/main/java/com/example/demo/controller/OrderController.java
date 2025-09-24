package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // @Autowired
    // private AlipayService alipayService;
    // @Autowired
    // private ProductService productService;
    // 创建订单
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Order>> addOrder(@RequestBody Order order, HttpServletRequest request) {
        // 从请求属性中获取用户ID (由拦截器放入)
        Long buyerId = (Long) request.getAttribute("userId");
        order.setBuyerId(buyerId);

        Order createdOrder = orderService.addOrder(order);
        return ResponseEntity.ok(ApiResponse.success(createdOrder, "订单创建成功"));
    }

    // 取消订单
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable Long orderId, HttpServletRequest request) {
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

    // 删除订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId, HttpServletRequest request) {
        // 从请求属性中获取用户ID
        Long buyerId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        boolean deleted = orderService.deleteOrder(orderId, buyerId, role);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "订单删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.error(500, "订单删除失败"));
        }
    }

    // 获取用户订单列表
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders(HttpServletRequest request) {
        // 从请求属性中获取用户ID
        Long buyerId = (Long) request.getAttribute("userId");

        List<Order> orders = orderService.getOrdersByBuyerId(buyerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    // 获取没有支付的订单
    @GetMapping("/unpaid")
    public ResponseEntity<ApiResponse<List<Order>>> getUnpaidOrders(HttpServletRequest request) {
        Long buyerid = (Long) request.getAttribute("userId");

        // 筛选出status为未支付的        
        List<Order> orders = orderService.getOrdersByBuyerId(buyerid).stream()
                .filter(order -> "未支付".equals(order.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
}


/*
     * 关于支付订单的接口
 */
// 对已有订单进行支付
// @PostMapping("/{orderId}/pay")
// public ResponseEntity<ApiResponse<String>> payOrder(@PathVariable Long orderId, HttpServletRequest request) {
//     try {
//         Order order = orderService.getOrderById(orderId);
//         Double price = productService.getProduct(order.getProductId()).getPrice();
//         String payUrl = alipayService.createOrder(orderId.toString(), price.toString(), "测试商品");
//         return ResponseEntity.ok(ApiResponse.success(payUrl));
//     } catch (AlipayApiException e) {
//         return ResponseEntity.ok(ApiResponse.error(500, "支付宝支付失败"));
//     }
// }
// // 异步通知的处理
// @PostMapping("/{orderId}/notify")
// public ResponseEntity<ApiResponse<Void>> handleNotify(@PathVariable Long orderId, HttpServletRequest request) {
//     // 处理支付宝异步通知
//     // TODO: 处理支付宝异步通知
//     OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
//     orderUpdateRequest.setOrderId(orderId);
//     orderUpdateRequest.setStatus("已下单");
//     orderService.updateOrder(orderUpdateRequest);
//     return ResponseEntity.ok(ApiResponse.success(null));
//     }
