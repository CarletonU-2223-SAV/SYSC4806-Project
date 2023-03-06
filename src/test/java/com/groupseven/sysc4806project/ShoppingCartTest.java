package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
        List<Book> books = new ArrayList<>();
        cart.setBooks(books);
        assertEquals(books, cart.getBooks());
    }

    @Test
    public void checkout() {
    }

    @Test
    public void addBook(){
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        cart.addBook(book);
        assertEquals(book, cart.getBook(book.getId()));
    }

    @Test
    public void removeBook(){
        ShoppingCart cart = new ShoppingCart();
        Book book1 = new Book();
        Book book2 = new Book();
        cart.removeBook(book2.getId());
        assertFalse(cart.getBooks().contains(book2));
    }

    @Test
    public void clearCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBook(new Book());
        }
        cart.clearCart();
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
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testCreateCart(){
        MultiValueMap<String, User> params = new LinkedMultiValueMap<>();
        User user = new User();
        params.add("user", user);
        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/carts/",
                params,
                Integer.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);

        ShoppingCart cart = shoppingCartRepository.findById(response.getBody()).orElseThrow();
        assertEquals(user, cart.getCustomer());
    }

    @Test
    public void testListBooksInCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBook(new Book());
        }
        shoppingCartRepository.save(cart);

        ResponseEntity<Book[]> response = restTemplate.getForEntity(
                "/api/carts/" + cart.getId() + "/books",
                Book[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        for (Book book : response.getBody()){
            assertTrue(cart.getBooks().contains(book));
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
        assertTrue(cart.getBooks().contains(book));
    }

    @Test
    public void testRemoveBookFromCart(){
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        cart.addBook(book);
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
        assertFalse(cart.getBooks().contains(book));
    }

    @Test
    public void testChangeOrderAmount() {
        ShoppingCart cart = new ShoppingCart();
        Book book = new Book();
        book.setOrderAmount(10);
        cart.addBook(book);
        shoppingCartRepository.save(cart);

        MultiValueMap<String, Integer> params = new LinkedMultiValueMap<>();
        params.add("orderAmount", 5);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/carts/" + cart.getId() + "/" + book.getId(),
                params,
                Boolean.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        cart = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertEquals(5, cart.getBook(book.getId()).getOrderAmount());
    }

    @Test
    public void testClearCart(){
        ShoppingCart cart = new ShoppingCart();
        for (int i = 0; i < 10; i++){
            cart.addBook(new Book());
        }
        this.shoppingCartRepository.save(cart);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/carts/" + cart.getId(),
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
        int id = cart.getId();
        shoppingCartRepository.save(cart);

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