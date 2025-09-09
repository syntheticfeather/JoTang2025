package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Result;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody String username, String password) {
        String[] Strs = {username, password};
        userService.register(Strs[0], Strs[1]);
        return Result.Success("register success");
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody String username, String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Result.Success("login success");
        } else {
            return Result.Fail("login failed");
        }
    }

}
