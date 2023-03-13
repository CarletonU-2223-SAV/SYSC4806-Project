package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void set_getUser_ID() {
        User user = new User();
        user.setId(4);
        assertEquals(4, user.getId());
    }

    @Test
    public void set_getName() {
        User user = new User();
        user.setName("Test");
        assertEquals("Test", user.getName());
    }

    @Test
    public void set_get_isAdmin() {
        User user = new User();
        user.setAdmin(true);
        assertTrue(user.isAdmin());
    }

    @Test
    public void testGetUser(){
        User user = new User();
        userRepository.save(user);

        ResponseEntity<User> response = restTemplate.getForEntity(
                "/api/users/" + user.getId(),
                User.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
    }

    @Test
    public void testCreateUser(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Kevin Smith");
        params.add("isAdmin", "true");
        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/users",
                params,
                Integer.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0, String.valueOf(response.getBody()));

        User user = userRepository.findById(response.getBody()).orElseThrow();
        assertEquals("Kevin Smith", user.getName());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testListBooksInCart(){
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        for (int i = 0; i < 10; i++){
            cart.addBookID(new Book().getId());
        }
        userRepository.save(user);

        ResponseEntity<Map<Integer, Integer>> response = restTemplate.exchange(
                "/api/users/" + user.getId() + "/books",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<Integer, Integer>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        for (int id : response.getBody().keySet()){
            assertTrue(cart.getBooks().containsKey(id));
        }
    }

    @Test
    public void testAddBookToCart(){
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        Book book = new Book();
        userRepository.save(user);
        bookRepository.save(book);

        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/api/users/" + user.getId() + "/" + book.getId(),
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = userRepository.findById(user.getId()).orElseThrow().getCart();
        book = bookRepository.findById(book.getId()).orElseThrow();
        assertTrue(cart.getBooks().containsKey(book.getId()));
    }

    @Test
    public void testRemoveBookFromCart(){
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        Book book = new Book();
        bookRepository.save(book);
        cart.addBookID(book.getId());
        userRepository.save(user);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/users/" + user.getId() + "/" + book.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = userRepository.findById(user.getId()).orElseThrow().getCart();
        assertFalse(cart.getBooks().containsKey(book.getId()));
    }

    @Test
    public void testChangeOrderAmount() {
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        Book book = new Book();
        book.setInventory(20);
        bookRepository.save(book);
        cart.addBookID(book.getId());
        cart.setOrderAmount(10, book.getId());
        userRepository.save(user);

        MultiValueMap<String, Integer> params = new LinkedMultiValueMap<>();
        params.add("orderAmount", 5);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/users/" + user.getId() + "/" + book.getId(),
                params,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = userRepository.findById(user.getId()).orElseThrow().getCart();
        assertEquals(5, cart.getOrderAmount(book.getId()));
    }

    @Test
    public void testClearCart(){
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        for (int i = 0; i < 10; i++){
            cart.addBookID(new Book().getId());
        }
        userRepository.save(user);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/users/clear/" + user.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = userRepository.findById(user.getId()).orElseThrow().getCart();
        assertTrue(cart.getBooks().isEmpty());
    }

    @Test
    public void testDeleteUser(){

        User user = new User();
        userRepository.save(user);
        int id = user.getId();

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/users/" + id,
                HttpMethod.DELETE,
                null,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        assertTrue(userRepository.findById(id).isEmpty());
    }
}