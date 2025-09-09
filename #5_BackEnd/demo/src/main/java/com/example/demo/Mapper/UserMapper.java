package com.example.demo.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.example.demo.Entity.User;

public interface UserMapper {

    @Insert("insert into user(username, password) values(#{username}, #{password})")
    public void register(String username, String password);

    @Select("select * from user where username = #{username}")
    public User login(String username);
}
