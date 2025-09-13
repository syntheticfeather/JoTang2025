package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Product {

    Long id;

    @NotBlank
    @Size(min = 1, max = 20, message = "标题长度必须在1-20")
    String title;

    String description;

    @NotBlank
    String type;

    @NotNull
    @Pattern(regexp = "^\\d+(\\.\\d+)?$") // 价格必须是正数字或正数加小数
    Double price;

    // 只有两个规则已售出和未售出    
    @Pattern(regexp = "^(已售出|未售出)$")
    String status;

    Long publisherId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;

}
