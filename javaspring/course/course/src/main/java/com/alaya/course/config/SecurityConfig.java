package com.alaya.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // 允许 H2 控制台 iframe
            .authorizeHttpRequests(auth -> auth
                // 公开资源：任何人都可以访问
                .requestMatchers("/", "/register", "/login", "/css/**", "/h2-console/**").permitAll()
                // 学生专用路径：需要 STUDENT 权限
                .requestMatchers("/student/**").hasAuthority("STUDENT")
                // 教师专用路径
                .requestMatchers("/teacher/**").hasAuthority("TEACHER")
                // 管理员专用路径
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                // 其他任何请求都需要认证
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}