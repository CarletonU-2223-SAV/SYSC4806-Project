package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookTest {
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
}