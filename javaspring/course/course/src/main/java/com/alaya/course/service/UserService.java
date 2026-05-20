package com.alaya.course.service;

import com.alaya.course.domain.User;
import com.alaya.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * 注册新用户（角色固定为 STUDENT）
     * @return true 注册成功，false 用户名已存在
     */
    public boolean register(String username, String password) {
        // 防御性编程：Controller 已做非空校验，此处作为最后防线
        if (password == null || password.trim().isEmpty()) return false;
        if (userRepository.existsByUsername(username)) return false;
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("STUDENT");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
}