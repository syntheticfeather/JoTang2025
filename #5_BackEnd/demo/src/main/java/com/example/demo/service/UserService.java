package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.PasswordResetRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.entity.User;

public interface UserService {

    public User register(RegisterRequest request);

    public LoginResponse login(LoginRequest request);

    public User getUserById(Long userId);

    public void resetPassword(Long userId, PasswordResetRequest request);

    public User upgradeAdmin(Long userId);

    public User updateUserName(UpdateUserRequest updateUserRequest, Long userId);

    public void updatePhone(Long userId, String phone);
}
