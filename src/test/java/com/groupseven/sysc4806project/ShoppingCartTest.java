package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void set_getCart_ID() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1);
        assertEquals(1, cart.getId());
    }

    @Test
    public void set_getBooks() {
        ShoppingCart cart = new ShoppingCart();
        Map<Integer, Integer> books = new HashMap<>();
        cart.setBooks(books);
        assertEquals(books, cart.getBooks());
    }

    @Test
    public void checkout() {
    }

    @Test
    public void get_addBookID(){
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        int id = book.getId();
        cart.addBookID(id);
        assertTrue(cart.getBooks().containsKey(id));
    }

    @Test
    public void removeBookID(){
        ShoppingCart cart = new ShoppingCart();
        Book book1 = new Book();
        Book book2 = new Book();
        cart.addBookID(book1.getId());
        cart.addBookID(book2.getId());
        assertTrue(cart.removeBookID(book2.getId()));
    }

    @Test
    public void set_getOrderAmount(){
        ShoppingCart cart = new ShoppingCart();
        Book book1 = new Book();
        int id = book1.getId();
        cart.addBookID(id);
        cart.setOrderAmount(10, id);
        assertEquals(10, cart.getOrderAmount(id));
    }
    @Test
    public void clearCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBookID(new Book().getId());
        }
        cart.clear();
        assertTrue(cart.getBooks().isEmpty());
    }
}