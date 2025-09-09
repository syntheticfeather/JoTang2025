package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Product {

    Long id;

    @NotBlank
    String title;

    String description;

    @NotBlank
    String type;

    @NotNull
    Double price;

    // 只有两个规则已售出和未售出    
    @Pattern(regexp = "^(已售出|未售出)$")
    String status;

    Long publisherId;

    LocalDateTime publishTime;

    LocalDateTime updateTime;

}
