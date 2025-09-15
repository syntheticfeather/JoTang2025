package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ValidatePhoneCodeRequest {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @NotBlank(message = "验证码不能为空")
    private String code;

}
