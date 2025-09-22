package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ValidateEmailCodeRequest {

    @NotBlank(message = "邮箱不能为空不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String code;

}
