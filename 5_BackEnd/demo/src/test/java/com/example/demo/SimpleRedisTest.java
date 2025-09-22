package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.example.demo.entity.User;

@SpringBootTest
public class SimpleRedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testStringSetAndGet() {
        // 设置值
        stringRedisTemplate.opsForValue().set("greeting", "Hello, Redis!");

        // 获取值并断言
        String value = stringRedisTemplate.opsForValue().get("greeting");
        assertEquals("Hello, Redis!", value);

        System.out.println("成功从Redis获取值: " + value);
    }

    @Test
    void testRedisTemplate() {
        redisTemplate.opsForValue().set("name", "John");
        String name = (String) redisTemplate.opsForValue().get("name");
        assertEquals("John", name);

        System.out.println("成功从Redis获取值: " + name);

        User user = new User(1L, "Tom", "123456", "USER", "13812345678",
                 null, null);
        redisTemplate.opsForValue().set("user", user);
        User user2 = (User) redisTemplate.opsForValue().get("user");
        assertEquals(user, user2);

        System.out.println("成功从Redis获取值: " + user2);
    }

    // redis中挑选拿，并且排序
    @Test
    void testSort() {
        redisTemplate.opsForValue().set("name1", "John");
        redisTemplate.opsForValue().set("name2", "Tom");
        redisTemplate.opsForValue().set("name3", "Lily");

        // 排序
        Set<String> keys = redisTemplate.keys("name*");
        List<String> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);

        // 取出排序后的结果
        String name1 = (String) redisTemplate.opsForValue().get(sortedKeys.get(0));
        String name2 = (String) redisTemplate.opsForValue().get(sortedKeys.get(1));
        String name3 = (String) redisTemplate.opsForValue().get(sortedKeys.get(2));
    }

}
