package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void set_getId() {
        Book book = new Book();
        book.setId(3);
        assertEquals(3, book.getId());
    }

    @Test
    public void set_getInventory() {
        Book book = new Book();
        book.setInventory(3);
        assertEquals(3, book.getInventory());
    }

    @Test
    public void set_getIsbn() {
        Book book = new Book();
        book.setIsbn("ISBN TEST");
        assertEquals("ISBN TEST", book.getIsbn());
    }

    @Test
    public void set_getDescription() {
        Book book = new Book();
        book.setDescription("Test description");
        assertEquals("Test description", book.getDescription());
    }

    @Test
    public void set_getTitle() {
        Book book = new Book();
        book.setTitle("Test title");
        assertEquals("Test title", book.getTitle());
    }

    @Test
    public void set_getAuthor() {
        Book book = new Book();
        book.setAuthor("Test Author");
        assertEquals("Test Author", book.getAuthor());
    }

    @Test
    public void set_getPublisher() {
        Book book = new Book();
        book.setPublisher("Test Publisher");
        assertEquals("Test Publisher", book.getPublisher());
    }

    @Test
    public void set_getGenre() {
        Book book = new Book();
        book.setGenre("A12");
        assertEquals("A12", book.getGenre());
    }

    @Test
    public void set_getPrice() {
        Book book = new Book();
        BigDecimal price = new BigDecimal("888.22");
        book.setPrice(price);
        assertEquals(price, book.getPrice());
    }

    @Test
    public void getImage() {
        Book book = new Book();
        book.setId(1);
        assertEquals("/book-img/1", book.getImageUrl());
    }

    @Test
    public void testGetBooksList() {
        for (int i = 0; i < 5; i++) {
            Book b = new Book();
            bookRepository.save(b);
        }

        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        ResponseEntity<Book[]> response = restTemplate.getForEntity("/api/books", Book[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        for (Book b : response.getBody()) {
            assertTrue(books.contains(b));
        }
    }

    @Test
    public void testGetBook() {
        Book b = new Book();
        bookRepository.save(b);

        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/" + b.getId(), Book.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(b, response.getBody());
    }

    @Test
    public void testGetMissingBook() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/api/books/99999999", Book.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateBook() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("isbn", "12345");
        params.add("title", "Humble Pi");
        params.add("description", "A book about math mistakes");
        params.add("author", "Matt Parker");
        params.add("publisher", "Riverhead Books");
        params.add("genre", "action");
        params.add("inventory", "50");
        params.add("price", "55.99");
        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/books",
                params,
                Integer.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);

        Book b = bookRepository.findById(response.getBody()).orElseThrow();
        assertEquals("12345", b.getIsbn());
        assertEquals("Humble Pi", b.getTitle());
        assertEquals("A book about math mistakes", b.getDescription());
        assertEquals("Matt Parker", b.getAuthor());
        assertEquals("Riverhead Books", b.getPublisher());
        assertEquals("action", b.getGenre());
        assertEquals(50, b.getInventory());
        assertEquals(new BigDecimal("55.99"), b.getPrice());
    }

    @Test
    public void testCreateBookWithoutOptionalParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("isbn", "54321");
        params.add("title", "Twilight");
        params.add("author", "Stephenie Meyer");
        params.add("publisher", "Little, Brown and Company");
        params.add("genre", "Action");
        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/books",
                params,
                Integer.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);

        Book b = bookRepository.findById(response.getBody()).orElseThrow();
        assertEquals("54321", b.getIsbn());
        assertEquals("Twilight", b.getTitle());
        assertEquals("", b.getDescription());
        assertEquals("Stephenie Meyer", b.getAuthor());
        assertEquals("Little, Brown and Company", b.getPublisher());
        assertEquals("Action", b.getGenre());
        assertEquals(0, b.getInventory());
        assertEquals(new BigDecimal("0.00"), b.getPrice());
    }

    @Test
    public void testUpdateBook() {
        Book b = new Book();
        b.setIsbn("98765");
        b.setTitle("Hungry Games");
        b.setDescription("A game about hunger");
        b.setAuthor("Suzan Collins");
        b.setPublisher("Scholar");
        b.setGenre("Fantasy");
        b.setInventory(0);
        this.bookRepository.save(b);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("isbn", "56789");
        params.add("title", "Hunger Games");
        params.add("description", "The games of hunger");
        params.add("author", "Suzanne Collins");
        params.add("publisher", "Scholastic");
        params.add("genre", "Sci-fi");
        params.add("inventory", "10");

        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/books/" + b.getId(),
                params,
                Boolean.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());

        b = this.bookRepository.findById(b.getId()).orElseThrow();

        assertEquals("56789", b.getIsbn());
        assertEquals("Hunger Games", b.getTitle());
        assertEquals("The games of hunger", b.getDescription());
        assertEquals("Suzanne Collins", b.getAuthor());
        assertEquals("Scholastic", b.getPublisher());
        assertEquals("Sci-fi", b.getGenre());
        assertEquals(10, b.getInventory());
    }

    @Test
    public void testUpdateMissingBook() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("isbn", "56789");
        params.add("title", "Hunger Games");
        params.add("description", "The games of hunger");
        params.add("author", "Suzanne Collins");
        params.add("publisher", "Scholastic");
        params.add("genre", "Fantasy");
        params.add("inventory", "10");
        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "/api/books/9999999",
                params,
                Boolean.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }

    @Test
    public void testDeleteBook() {
        Book b = new Book();
        this.bookRepository.save(b);
        int id = b.getId();

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/books/delete/" + id,
                HttpMethod.POST,
                null,
                Boolean.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
        assertTrue(this.bookRepository.findById(id).isEmpty());
    }

    @Test
    public void testDeleteMissingBook() {
        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/books/delete/9999999",
                HttpMethod.POST,
                null,
                Boolean.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
    }
}