package com.groupseven.sysc4806project;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class HttpTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testBooksInCart() throws Exception {
        Integer userId = 1;
        Map<Integer, Integer> bookIds = Map.of(1,2,3,4);
        when(userService.listBooksInCart(userId)).thenReturn(bookIds);
        mockMvc.perform(get("/cart")
                        .cookie(new Cookie("userId", userId.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddBookToCart() throws Exception{
        Integer userId = 1;
        Integer bookId = 2;
        when(userService.addBookToCart(userId, bookId)).thenReturn(true);
        mockMvc.perform(post("/cart/add")
                        .cookie(new Cookie("userId", userId.toString()))
                        .param("bookId", bookId.toString()))
                .andExpect(status().isFound());
    }

    @Test
    public void testRemoveBookFromCart() throws Exception{
        Integer userId = 1;
        Integer bookId = 2;
        when(userService.removeBookFromCart(userId, bookId)).thenReturn(true);
        mockMvc.perform(post("/cart/remove")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString()))
                .andExpect(status().isFound());
    }

    @Test
    public void testClearCart() throws Exception{
        Integer userId = 1;
        when(userService.clearCart(userId)).thenReturn(true);
        mockMvc.perform(post("/cart/clear")
                        .param("userId", userId.toString()))
                .andExpect(status().isFound());
    }

    @Test
    public void testChangeOrderAmountInCart() throws Exception{
        Integer userId = 1;
        Integer bookId = 2;
        Integer orderAmount = 3;
        when(userService.changeOrderAmount(userId, bookId, orderAmount)).thenReturn(true);
        mockMvc.perform(post("/cart/changeOrdAmo")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .param("orderAmount", orderAmount.toString()))
                .andExpect(status().isFound());

    }
    
    @Test
    public void testGoToCheckoutPage() throws Exception {
        Integer userId = 1;
        Map<Integer, Integer> bookIds = Map.of(1,2,3,4);
        when(userService.listBooksInCart(userId)).thenReturn(bookIds);
        mockMvc.perform(get("/cart/goToCOH")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckout() throws Exception {
        Integer userId = 1;
        Map<Integer, Integer> bookIds = Map.of(1,2,3,4);
        when(userService.checkoutUser(userId)).thenReturn(true);
        mockMvc.perform(post("/cart/COH")
                        .param("userId", userId.toString()))
                .andExpect(status().isFound());
    }
    
    @Test
    public void recommendation() throws Exception {
        User user = new User();
        Integer userId = 1;
        List<Book> lst = new ArrayList<>();
        when(userService.getUser(user.getId())).thenReturn(user);
        when(userService.recommendedBooks(userId)).thenReturn(lst);
        this.mockMvc.perform(get("/user/recommendation").cookie(new Cookie("userId", user.getId() +"")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no books to display")));
    }
}
