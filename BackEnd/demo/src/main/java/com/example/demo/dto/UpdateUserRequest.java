package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "新用户名长度必须在3-20之间")
    private String username;

    @Pattern(regexp = "^1[34578]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    // @NotBlank(message = "邮箱不能为空")
    // @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "邮箱格式不正确")
    // private String email;
}
