package com.example.demo.service.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.PasswordResetRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // 用户注册
    @Transactional
    @Override
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.countByUsername(request.getUsername()) > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        if ("ADMIN".equals(request.getRole())) {
            if (!"654321".equals(request.getAdminRegister())) {
                throw new BusinessException(400, "只有管理员才能注册管理员");
            }
            user.setRole("ADMIN");
        }
        userMapper.register(user);
        user = userMapper.selectByUsername(request.getUsername());
        return user;
    }

    // 用户登录
    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return new LoginResponse(user, token);
    }

    // 根据ID获取用户
    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        return user;

    }

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @param request 密码重置请求
     * @return 是否成功
     */
    @Transactional
    @Override
    public void resetPassword(Long userId, PasswordResetRequest request) {
        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(400, "新密码和确认密码不一致");
        }
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (request.getUsername() != null) {
            user = userMapper.selectByUsername(request.getUsername());
        }
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        // 验证原密码是否正确
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }
        // 加密新密码
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());

        // 更新密码
        int affectedRows = userMapper.update(user);

        if (affectedRows != 1) {
            throw new BusinessException(500, "更新密码失败");
        }
    }

    @Override
    public User upgradeAdmin(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if (!"USER".equals(user.getRole())) {
            throw new BusinessException(400, "用户已是管理员");
        }
        user.setRole("ADMIN");
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        return user;
    }

    @Override
    public User updateUserInfo(UpdateUserRequest updateUserRequest, Long userId) {
        String username = updateUserRequest.getUsername();
        User user = userMapper.selectById(userId);

        if (username.equals(user.getUsername())) {
            throw new BusinessException(400, "新用户名与旧用户名相同");
        }
        if (userMapper.selectByUsername(username) != null) {
            throw new BusinessException(400, "新用户名已存在");
        }
        user.setUsername(username);
        user.setUpdateTime(LocalDateTime.now());
        user.setPhone(updateUserRequest.getPhone());
        // user.setEmail(updateUserRequest.getEmail());
        userMapper.update(user);
        return userMapper.selectById(userId);
    }

    @Override
    public void updatePhone(Long userId, String phone) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if (userMapper.countByPhone(phone) > 0) {
            throw new BusinessException(400, "手机号已被注册，请直接登录");
        }
        user.setPhone(phone);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateEmail(Long userId, String email) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }
        // user.setEmail(email);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

}
