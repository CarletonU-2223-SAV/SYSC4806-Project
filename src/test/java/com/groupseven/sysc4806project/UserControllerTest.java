package com.groupseven.sysc4806project;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Username:")));
    }

    @Test
    public void signUp() throws Exception {
        this.mockMvc.perform(get("/user/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign Up")));
    }

    @Test
    public void logOut() throws Exception {
        this.mockMvc.perform(get("/user/log-out"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void getUserForLogin() throws Exception {
        String name = "Beta";
        when(userService.createUser(name, false)).thenReturn(1);
        this.mockMvc.perform(get("/user/get-user").param("username", name))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Username doesn't exist, please try again")));
    }

    @Test
    public void recommendation() throws Exception {
        String name = "Beta";
        Integer userId = 1;
        when(userService.createUser(name, false)).thenReturn(1);
        this.mockMvc.perform(get("/user/recommendation").cookie(new Cookie("userId", userId.toString()))
                        .param("username", name))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"));
    }
}
