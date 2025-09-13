package com.example.demo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.exception.BusinessException;
import com.example.demo.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");

        // 登录验证
        if (token == null) {
            throw new BusinessException(401, "请先登录");
        }

        // 过期验证
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }

        // 将用户ID存入请求属性
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        request.setAttribute("role", role);
        request.setAttribute("userId", userId);
        return true;
    }
}
