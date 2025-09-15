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
    long register(User user);

    // 登录
    @Select("select * from user where username = #{username}")
    User login(String username);

    // 删除用户
    @Delete("Delete * FROM user WHERE id = #{id}")
    int deleteUser(long id);

    // 根据ID查询用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    // 检查用户名是否存在
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(String username);

    // 检查手机号是否已绑定
    @Select("SELECT COUNT(*) FROM user WHERE phone = #{phone}")
    int countByPhone(String phone);

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    // 全部更新
    @Update("UPDATE user SET update_time = #{now}, username = #{username}, password = #{password}, role = #{role}, phone = #{phone} WHERE id = #{id}")
    int update(User user);

}
