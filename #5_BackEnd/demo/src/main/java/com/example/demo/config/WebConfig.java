package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.handler.JwtInterceptor;
import com.example.demo.handler.RoleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login", "/users/register")
                .order(1);

        // 角色权限拦截器（验证角色权限）
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login", "/users/register")
                .order(10);
    }

    @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            // 将常见的前端路由映射到index.html
            registry.addViewController("/").setViewName("forward:/index.html");
            registry.addViewController("/login").setViewName("forward:/login.html");
            registry.addViewController("/register").setViewName("forward:/register.html");
            registry.addViewController("/dashboard").setViewName("forward:/admin.html");
            
            // 处理所有未匹配的路由，返回index.html（支持SPA）
            registry.addViewController("/{spring:\\w+}")
                    .setViewName("forward:/index.html");
            registry.addViewController("/**/{spring:\\w+}")
                    .setViewName("forward:/index.html");
        }

}
