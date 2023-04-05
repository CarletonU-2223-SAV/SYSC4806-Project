package com.groupseven.sysc4806project;

import jakarta.servlet.http.Cookie;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.TreeMap;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @Test
    public void listOrders() throws Exception {
        User user = new User();
        OrderHistory orderHistory = new OrderHistory();

        when(userService.getUser(user.getId())).thenReturn(user);
        when(userService.getOrderHistory(user.getId())).thenReturn(orderHistory);

        mockMvc.perform(get("/orders")
                        .cookie(new Cookie("userId", String.valueOf(user.getId()))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No Orders have been made")));
    }


}
