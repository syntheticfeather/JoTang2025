package com.example.RabbitMQ.mq.consumer;

import java.io.IOException;

import com.example.RabbitMQ.utils.RabbitUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.RabbitMQ.config.RabbitMQConfig;
import com.example.RabbitMQ.entity.Order;
import com.rabbitmq.client.Channel;

@Component
public class EmailConsumer {

    @Autowired
    private RabbitUtil rabbitUtil;
    /*
     * 监听 order.created 队列，收到消息后，调用 handleOrderCreated 方法处理
     * 这里的 message 就是 RabbitMQ 传递过来的消息，包含了 delivery tag
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderCreated(Order order, Message message, Channel channel) throws IOException {
        // 获取 delivery tag,这是 RabbitMQ 用来标识消息的唯一 ID
        long tag = rabbitUtil.getTag(message);

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
