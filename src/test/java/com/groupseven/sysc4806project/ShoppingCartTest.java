package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ShoppingCartTest {

    @Test
    public void set_getCart_ID() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCart_ID(1);
        assertEquals(1, cart.getCart_ID());
    }

    @Test
    public void set_getCustomer() {
        User customer = new User();
        ShoppingCart cart = new ShoppingCart();
        customer.setCart(cart);
        cart.setCustomer(customer);
        assertEquals(customer.getUser_ID(), cart.getCustomer().getUser_ID());
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
}