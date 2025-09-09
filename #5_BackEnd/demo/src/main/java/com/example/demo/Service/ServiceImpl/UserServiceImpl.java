package com.example.demo.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password) {
        userMapper.register(username, password);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.login(username);
        if (password.equals(user.getPassword())) {
            return userMapper.login(username);
        } else {
            return null;
        }
    }

}
