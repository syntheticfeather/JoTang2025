package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Order;

public interface OrderService {

    public Order addOrder(Order order);

    public Order cancelOrder(Long orderId, Long buyerId);

    public Order getOrderById(Long id);

    public List<Order> getOrdersByBuyerId(Long buyerId);

    public List<Order> getOrdersByProductId(Long productId);

    public boolean deleteOrder(Long id, Long buyerId);
}
