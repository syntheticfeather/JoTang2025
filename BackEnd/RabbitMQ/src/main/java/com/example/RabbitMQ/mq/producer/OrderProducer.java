package com.example.RabbitMQ.mq.producer;

import com.example.RabbitMQ.config.RabbitMQConfig;
import com.example.RabbitMQ.entity.Order;
import com.example.RabbitMQ.utils.RabbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    @Autowired
    private RabbitUtil rabbitUtil;

    public void sendOrderCreated(Order order) {
        rabbitUtil.send(RabbitMQConfig.ORDER_QUEUE, RabbitMQConfig.ORDER_ROUTING_KEY, order);
        System.out.println(" [x] Sent OrderCreated event: " + order.getId());
    }
}
