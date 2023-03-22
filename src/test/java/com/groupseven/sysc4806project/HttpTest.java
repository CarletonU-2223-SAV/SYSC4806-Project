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
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Test
    public void testHomePageNoBooks() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no books to display")));
    }

    @Test
    public void testHomePageWithBook() throws Exception {
        Book b = new Book();
        b.setTitle("My Book Title");
        when(bookService.list()).thenReturn(List.of(b));
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("My Book Title")));
    }

    @Test
    public void testFilter() throws Exception {
        Book b = new Book();
        b.setTitle("My Book 2");
        b.setIsbn("55555");
        b.setAuthor("Arthur");
        b.setPublisher("PubCo");
        b.setGenre("Dark");
        when(bookService.list()).thenReturn(List.of(b));
        this.mockMvc.perform(get("/home?title=My Book 2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Arthur")));

        this.mockMvc.perform(get("/home?title=Other Book&publisher=PubCo&genre=Dark"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no books to display")));

        this.mockMvc.perform(get("/home?isbn=55555"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("My Book 2")));
    }

    @Test
    public void testAddBookPage() throws Exception {
        this.mockMvc.perform(post("/home/add-book?isbn=Test&title=Test title&description=Test description&author=Test author&publisher=Test publisher&genre=action&inventory=4"))
                .andExpect(status().isFound());
    }

    @Test
    public void testEditBookPage() throws Exception {
        Book test = new Book();
        test.setTitle("Test");
        test.setInventory(5);
        test.setDescription("Test description");
        test.setAuthor("Test author");
        test.setIsbn("Test");
        test.setPublisher("Test publisher");
        test.setGenre("Test genre");
        when(bookService.list()).thenReturn(List.of(test));
        this.mockMvc.perform(post("/home/edit-book?description=Test description2&inventory=5&book_id=1"))
                .andExpect(status().isFound());
    }

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
                .andExpect(status().isFound());
    }

    @Test
    public void getUserForLogin() throws Exception {
        String name = "Beta";
        when(userService.createUser(name, false)).thenReturn(1);
        this.mockMvc.perform(get("/user/get-user").param("username", name))
                .andExpect(status().isOk());
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
        String name = "Beta";
        Integer userId = 1;
        when(userService.createUser(name, false)).thenReturn(1);
        this.mockMvc.perform(get("/user/recommendation").cookie(new Cookie("userId", userId.toString()))
                        .param("username", name))
                .andExpect(status().isFound());
    }
}
