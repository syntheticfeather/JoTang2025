package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.RequireRole;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.PasswordResetRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.ValidatePhoneCodeRequest;
import com.example.demo.entity.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.service.AuthCodeService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthCodeService authCodeService;

    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success(user, "注册成功"));
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "登录成功"));
    }

    // 获取当前用户信息
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "未授权访问"));
        }
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/resetpwd")
    public ResponseEntity<ApiResponse<String>> resetPassword(HttpServletRequest request, @RequestBody PasswordResetRequest passwordResetRequest) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "未授权访问"));
        }
        userService.resetPassword(userId, passwordResetRequest);
        return ResponseEntity.ok(ApiResponse.success(passwordResetRequest.getNewPassword(), "密码重置成功"));
    }

    // 管理员升级为超级管理员
    @RequireRole("ADMIN")
    @PutMapping("/upgrade")
    public ResponseEntity<ApiResponse<User>> upgradeAdmin(@RequestParam long userId) {
        User user = userService.upgradeAdmin(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "用户升级为管理员"));
    }

    /*
     * 更新名称(手机号业务重新设计)
     * 更新手机号时需要重新的短信验证
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        User user = userService.updateUserName(updateUserRequest, (Long) request.getAttribute("userId"));
        return ResponseEntity.ok(ApiResponse.success(user, "用户信息更新成功"));
    }

    /*
     * 发送手机号验证码
     */
    @PostMapping("/sendPhoneCode")
    public ResponseEntity<ApiResponse<String>> snedPhoneCode(@RequestBody String phone) {
        authCodeService.savePhoneAuthCode(phone);
        return ResponseEntity.ok(ApiResponse.success(phone, "手机验证码已发送"));
    }

    /*
     * 验证手机短信验证码
     */
    @PostMapping("/validatePhoneCode")
    public ResponseEntity<ApiResponse<String>> validatePhoneCode(@RequestBody ValidatePhoneCodeRequest validatePhoneCodeRequest,
            HttpServletRequest request) {
        authCodeService.validateCode(validatePhoneCodeRequest.getPhone(), validatePhoneCodeRequest.getCode());
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePhone(userId, validatePhoneCodeRequest.getPhone());
        return ResponseEntity.ok(ApiResponse.success(validatePhoneCodeRequest.getPhone(), "手机验证码验证成功"));
    }
}
