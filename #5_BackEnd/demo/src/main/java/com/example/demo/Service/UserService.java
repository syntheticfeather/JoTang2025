package com.example.demo.Service;

import com.example.demo.Entity.User;

public interface UserService {

    public void register(String username, String password);

    public User login(String username, String password);

}
