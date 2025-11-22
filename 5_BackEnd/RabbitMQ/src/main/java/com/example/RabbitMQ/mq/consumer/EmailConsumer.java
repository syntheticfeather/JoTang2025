package com.example.RabbitMQ.mq.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.RabbitMQ.config.RabbitMQConfig;
import com.example.RabbitMQ.entity.Order;
import com.rabbitmq.client.Channel;

@Component
public class EmailConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderCreated(Order order, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        try {
            // 模拟发邮件（实际可调用邮件服务）
            System.out.println(" [📧] Sending email for order: " + order.getId()
                    + " to user: " + order.getUserId());

            // 模拟可能失败（比如订单ID为999时）
            if (order.getId() != null && order.getId() == 999L) {
                throw new RuntimeException("Simulated email failure!");
            }

            // 成功：ACK
            channel.basicAck(tag, false);
            System.out.println(" [✅] Email sent for order: " + order.getId());

        } catch (Exception e) {
            System.err.println(" [❌] Failed to send email: " + e.getMessage());
            // 拒绝且不 requeue → 进入 DLQ
            channel.basicNack(tag, false, false);
        }
    }
}
