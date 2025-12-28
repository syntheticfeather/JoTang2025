package com.example.demo.dto;

import com.example.demo.entity.User;

import lombok.Data;

@Data
public class LoginResponse {

    private Long id;
    private String username;
    private String role;
    private String phone;
    private String token;

    public LoginResponse(User user, String token) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.phone = user.getPhone();
        this.token = token;
    }
}
