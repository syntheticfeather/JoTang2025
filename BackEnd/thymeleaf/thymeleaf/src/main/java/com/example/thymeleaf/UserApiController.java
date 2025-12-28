package com.example.thymeleaf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController // ← 关键：返回 JSON，不是 HTML
@RequestMapping("/api/user")
public class UserApiController {

    @GetMapping("/current")
    public Map<String, Object> getCurrentUser() {
        // 模拟当前用户（实际可从 SecurityContext 或 Session 获取）
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1001);
        user.put("name", "张三");
        user.put("role", "borrower");
        return user; // Spring Boot 自动转为 JSON
    }
}