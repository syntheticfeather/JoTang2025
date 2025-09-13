package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {

    // 注册用户
    @Insert("insert into user(username, password, role, phone, create_time, update_time)"
            + " values(#{username}, #{password}, #{role}, #{phone}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public long register(User user);

    // 登录
    @Select("select * from user where username = #{username}")
    public User login(String username);

    // 删除用户
    @Delete("Delete * FROM user WHERE id = #{id}")
    int deleteUser(long id);

    // 更新密码
    @Update("UPDATE user SET password = #{password}, update_time = #{updateTime} WHERE id = #{id}")
    int updatePassword(Long id, String password, LocalDateTime updateTime);

    // 根据ID查询用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(String username);

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Update("UPDATE user SET role = #{role} WHERE id = #{id}")
    public void upgradeAdmin(User user);

    @Update("UPDATE user SET phone = #{phone}, update_time = #{now}, username = #{username} WHERE id = #{id}")
    public void updateUser(Long id, String username, String phone, LocalDateTime now);

}
