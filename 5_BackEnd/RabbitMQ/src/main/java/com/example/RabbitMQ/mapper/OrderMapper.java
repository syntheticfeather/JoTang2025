package com.example.RabbitMQ.mapper;

import com.example.RabbitMQ.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insert(Order order);
}
