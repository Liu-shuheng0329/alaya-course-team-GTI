package com.alaya.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserAuthenticationAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== US-1 注册学生账号 ====================

    @Test
    void shouldRegisterSuccess_WhenValidInput() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "student1")
                .param("password", "123456")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void shouldFail_WhenUsernameAlreadyExists() throws Exception {
        // 第一次注册
        mockMvc.perform(post("/register")
                .param("username", "duplicate")
                .param("password", "pass")
                .with(csrf()));

        // 第二次注册相同用户名
        mockMvc.perform(post("/register")
                .param("username", "duplicate")
                .param("password", "newpass")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void shouldShowError_WhenPasswordIsEmpty() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "test")
                .param("password", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "密码不能为空"));
    }

    // ==================== US-2 用户登录 ====================

    @Test
    void shouldLoginSuccess_WhenValidCredentials() throws Exception {
        // 先注册一个用户
        mockMvc.perform(post("/register")
                .param("username", "logintest")
                .param("password", "pass")
                .with(csrf()));

        // 执行登录
        mockMvc.perform(post("/login")
                .param("username", "logintest")
                .param("password", "pass")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    // ==================== US-3 角色菜单与权限隔离 ====================

    @Test
    @WithMockUser(authorities = "STUDENT")
    void shouldShowStudentMenu_WhenStudentLoggedIn() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("浏览课程")))
                .andExpect(content().string(containsString("我的课表")));
    }

    @Test
    @WithMockUser(authorities = "TEACHER")
    void shouldReturn403_WhenTeacherAccessStudentPage() throws Exception {
        mockMvc.perform(get("/student/courses"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRedirectToLogin_WhenUnauthenticatedAccessProtectedPage() throws Exception {
        mockMvc.perform(get("/student/courses"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}