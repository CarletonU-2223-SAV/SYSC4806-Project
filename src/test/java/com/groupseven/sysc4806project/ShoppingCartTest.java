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
    public void set_getCustomer() {
        User customer = new User();
        ShoppingCart cart = new ShoppingCart();
        cart.setCustomer(customer);
        assertEquals(customer.getId(), cart.getCustomer().getId());
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

    @Test
    public void testGetCart(){
        ShoppingCart cart = new ShoppingCart();
        shoppingCartRepository.save(cart);

        ResponseEntity<ShoppingCart> response = restTemplate.getForEntity(
                "/api/carts/" + cart.getId(),
                ShoppingCart.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cart.getId(), response.getBody().getId());
    }

    @Test
    public void testCreateCart(){
        User user = new User();
        userRepository.save(user);
        ResponseEntity<Integer> response = restTemplate.getForEntity(
                "/api/carts/create/" + user.getId(),
                Integer.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);

        ShoppingCart cart = shoppingCartRepository.findById(response.getBody()).orElseThrow();
        user = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user.getId(), cart.getCustomer().getId());
    }

    @Test
    public void testListBooksInCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBookID(new Book().getId());
        }
        shoppingCartRepository.save(cart);

        ResponseEntity<Map<Integer, Integer>> response = restTemplate.exchange(
                "/api/carts/" + cart.getId() + "/books",
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
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        shoppingCartRepository.save(cart);
        bookRepository.save(book);

        ResponseEntity<Boolean> response = restTemplate.getForEntity(
                "/api/carts/" + cart.getId() + "/" + book.getId(),
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        book = bookRepository.findById(book.getId()).orElseThrow();
        assertTrue(cart.getBooks().containsKey(book.getId()), String.valueOf(cart.getBooks()));
    }

    @Test
    public void testRemoveBookFromCart(){
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        bookRepository.save(book);
        cart.addBookID(book.getId());
        shoppingCartRepository.save(cart);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/carts/" + cart.getId() + "/" + book.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        cart = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertFalse(cart.getBooks().containsKey(book.getId()));
    }

    @Test
    public void testChangeOrderAmount() {
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        book.setInventory(20);
        bookRepository.save(book);
        cart.addBookID(book.getId());
        cart.setOrderAmount(10, book.getId());
        shoppingCartRepository.save(cart);

        MultiValueMap<String, Integer> params = new LinkedMultiValueMap<>();
        params.add("orderAmount", 5);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/carts/" + cart.getId() + "/" + book.getId(),
                params,
                Boolean.class
        );
        cart = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody(), String.valueOf(cart.getBooks()));

        assertEquals(5, cart.getOrderAmount(book.getId()));
    }

    @Test
    public void testClearCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBookID(new Book().getId());
        }
        this.shoppingCartRepository.save(cart);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/carts/clear/" + cart.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        cart = this.shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertTrue(cart.getBooks().isEmpty());
    }

    @Test
    public void testDeleteCart(){
        ShoppingCart cart = new ShoppingCart();
        shoppingCartRepository.save(cart);
        int id = cart.getId();

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/carts/" + id,
                HttpMethod.DELETE,
                null,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        assertTrue(shoppingCartRepository.findById(id).isEmpty());
    }
}