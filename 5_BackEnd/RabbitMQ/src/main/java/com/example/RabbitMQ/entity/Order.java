package com.example.RabbitMQ.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private String userId;
    private String productId;
    private BigDecimal amount;
    private LocalDateTime createTime;    
}
