package com.example.RabbitMQ.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RabbitMQ.entity.Order;
import com.example.RabbitMQ.mapper.OrderMapper;
import com.example.RabbitMQ.mq.producer.OrderProducer;

@Service
public class OrderService {

    @Autowired
    private  OrderMapper orderMapper;
    @Autowired
    private  OrderProducer orderProducer;

    @Transactional
    public Order createOrder(Order order) {
        orderMapper.insert(order); // 保存到 PostgreSQL
        orderProducer.sendOrderCreated(order); // 发送 MQ
        return order;
    }
}
