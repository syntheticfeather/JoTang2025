package com.example.demo.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderUpdateRequest;
import com.example.demo.entity.Order;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;

    // 创建订单
    @Transactional
    @Override
    public Order addOrder(Order order) {
        // 检查商品是否存在
        if (productMapper.get(order.getProductId()) == null) {
            throw new ResourceNotFoundException("商品不存在");
        }

        // 设置订单初始状态和时间
        order.setStatus("未支付");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 插入订单
        int affectedRows = orderMapper.add(order);
        if (affectedRows != 1) {
            throw new BusinessException(500, "订单创建失败");
        }

        // 返回完整的订单信息
        return order;
    }

    // 取消订单
    @Transactional
    @Override
    public Order cancelOrder(Long orderId, Long userId, String role) {
        // 查询订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("订单不存在");
        }
        // 检查权限：只有买家可以取消自己的订单
        // ADMIN可以取消所有订单
        if (!"ADMIN".equals(role) && !order.getBuyerId().equals(userId)) {
            throw new BusinessException(403, "无权取消此订单");
        }

        // 检查订单状态：只有"已下单"的订单可以取消
        if (!"已下单".equals(order.getStatus())) {
            throw new BusinessException(400, "订单状态不允许取消");
        }
        // TODO
        // 还需有退款的逻辑
        order.setUpdateTime(LocalDateTime.now());
        order.setStatus("已取消");
        // 更新订单状态
        orderMapper.update(order);

        // 返回更新后的订单
        return orderMapper.selectById(orderId);
    }

    @Transactional
    @Override
    public Order updateOrder(OrderUpdateRequest orderUpdateRequest) {
        Order order = orderMapper.selectById(orderUpdateRequest.getOrderId());
        if (order == null) {
            throw new ResourceNotFoundException("订单不存在");
        }
        order.setStatus(orderUpdateRequest.getStatus());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
        return order;
    }

    // 根据ID获取订单
    @Override
    public Order getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new ResourceNotFoundException("订单不存在");
        }
        return order;
    }

    // 获取用户的订单列表
    @Override
    public List<Order> getOrdersByBuyerId(Long buyerId) {
        return orderMapper.selectByBuyerId(buyerId);
    }

    // 获取商品的订单列表
    @Override
    public List<Order> getOrdersByProductId(Long productId) {
        return orderMapper.selectByProductId(productId);
    }

    // 删除订单
    @Transactional
    @Override
    public boolean deleteOrder(Long id, Long buyerId, String role) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new ResourceNotFoundException("订单不存在");
        }

        // 检查权限：只有买家可以删除自己的订单
        if (!"ADMIN".equals(role) && !order.getBuyerId().equals(buyerId)) {
            throw new BusinessException(403, "无权删除此订单");
        }

        int affectedRows = orderMapper.deleteById(id);
        return affectedRows > 0;
    }
}
