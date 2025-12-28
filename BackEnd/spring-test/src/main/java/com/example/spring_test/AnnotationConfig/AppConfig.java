package com.example.spring_test.AnnotationConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.spring_test.AnnotationConfig.Component.MeddleComponent;
import com.example.spring_test.User;

// 扫描，然后注入
@Configuration
@ComponentScan
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        MeddleComponent component = context.getBean(MeddleComponent.class);

        // 测试普通Bean对象
        component.doSomething();
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());

        // 
        KeyBean keyBean = context.getBean(KeyBean.class);
        System.out.println(keyBean.key);

        // 测试Bean对象的字段注入
        MailService mailService = context.getBean(MailService.class);
        mailService.getKey();
    }
}
