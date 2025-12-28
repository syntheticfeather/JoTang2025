package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderUpdateRequest {

    private Long OrderId;
    private String status;
}
