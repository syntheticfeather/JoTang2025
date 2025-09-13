package com.example.demo.handler;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.annotation.RequireRole;
import com.example.demo.exception.BusinessException;
import com.example.demo.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RoleInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查handler是否是Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查方法上是否有@RequireRole注解
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            // 如果没有@RequireRole注解，则放行
            return true;
        }

        String userRole = request.getAttribute("role").toString();
        // 检查用户角色是否满足要求
        String[] requiredRoles = requireRole.value();
        boolean hasPermission = Arrays.stream(requiredRoles)
                .anyMatch(role -> role.equals(userRole));

        if (!hasPermission) {
            throw new BusinessException(403, "权限不足，需要角色: " + Arrays.toString(requiredRoles));
        }

        return true;
    }

}
