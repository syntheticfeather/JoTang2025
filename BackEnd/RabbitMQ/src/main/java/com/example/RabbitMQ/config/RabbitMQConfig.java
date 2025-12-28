package com.example.RabbitMQ.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "order.created";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.create";

    public static final String DLQ = "order.dlq"; // 死信队列
    public static final String DLX = "dlx"; // 死信交换机

    /*
     * 定义订单队列orderQueue()和业务交换机orderExchange()
     * 并进行订单队列和业务交换机的绑定
     * public Queue orderQueue()
     * public DirectExchange orderExchange()
     * public Binding orderBinding()
     */
    @Bean
    public Queue orderQueue() {
        // 定义一个持久化的订单队列，并配置死信交换机和路由键
        return QueueBuilder.durable(ORDER_QUEUE)
                // 失败的消息，转移到死信交换机，并且设置路由键为DLQ。
                .deadLetterExchange(DLX)
                .deadLetterRoutingKey(DLQ)
                .build();
    }

    @Bean
    public DirectExchange orderExchange() {
        // 定义业务交换机，用于接收订单创建消息
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding() {
        // 绑定订单队列到业务交换机
        // 规则：路由键为"order.create"的消息会被路由到订单队列
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    /*
     * 定义死信队列dlq()和死信交换机dlx()
     * 并进行DLQ路由键的绑定
     * public Queue dlq()
     * public DirectExchange dlx()
     * public Binding dlqBinding()
     */
    @Bean
    public Queue dlq() {
        // 定义一个持久化的死信队列
        return QueueBuilder
                .durable(DLQ)
                .build();
    }

    @Bean
    public DirectExchange dlx() {
        // 定义死信交换机
        return new DirectExchange(DLX);
    }

    @Bean
    public Binding dlqBinding() {
        // 凡是死信交换机dlx()中的路由键为DLQ的消息，都将被转移到死信队列dlq()中
        return BindingBuilder.bind(dlq()) // 死信列队
                .to(dlx()) // 死信交换机
                .with(DLQ); // 路由键
    }

}
