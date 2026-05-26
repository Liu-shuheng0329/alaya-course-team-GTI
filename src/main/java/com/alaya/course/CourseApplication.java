package com.alaya.course;

import com.alaya.course.domain.User;
import com.alaya.course.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class CourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }

    @Bean
    public CommandLineRunner initTeachers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String[] teacherNames = {"teacher1", "teacher2"};
            for (String name : teacherNames) {
                User teacher = userRepository.findByUsername(name).orElse(new User());
                teacher.setUsername(name);
                teacher.setPassword(passwordEncoder.encode("teacher123"));
                teacher.setRole("TEACHER");
                if (teacher.getCreatedAt() == null) {
                    teacher.setCreatedAt(LocalDateTime.now());
                }
                userRepository.save(teacher);
                System.out.println("✅ 教师账号 " + name + " 已就绪（密码 teacher123）");
            }
        };
    }
}