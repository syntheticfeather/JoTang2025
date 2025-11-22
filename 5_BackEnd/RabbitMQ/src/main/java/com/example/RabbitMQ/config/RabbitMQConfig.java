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
    public static final String DLQ = "order.dlq";
    public static final String DLX = "dlx";

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public DirectExchange dlx() {
        return new DirectExchange(DLX);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(dlx()).with(DLQ);
    }
}
