package com.example.RabbitMQ.mq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.RabbitMQ.config.RabbitMQConfig;
import com.example.RabbitMQ.entity.Order;

@Component
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreated(Order order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, order);
        System.out.println(" [x] Sent OrderCreated event: " + order.getId());
    }
}
