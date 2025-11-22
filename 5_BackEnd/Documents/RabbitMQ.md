# RabbitMQ

## docker部署

```yml
rabbitmq:
    image: rabbitmq:4.2.0-management
    container_name: rabbitmq
    restart: unless-stopped
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin123
      # 可选：自定义 vhost
      # RABBITMQ_DEFAULT_VHOST: my_vhost
    ports:
      - "5672:5672"   # AMQP 客户端通信端口
      - "15672:15672" # Web 管理界面
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
      - rabbitmq_log:/var/log/rabbitmq
    networks:
      - messaging
```

## 什么是RabbitMQ

RabbitMQ是一个开源的AMQP（Advanced Message Queuing Protocol）实现，它是用于在分布式系统中存储和转发消息的消息队列。RabbitMQ支持多种消息队列协议，包括AMQP、STOMP、MQTT等。RabbitMQ的主要特征包括：