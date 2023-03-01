package com.groupseven.sysc4806project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ShoppingCartTest {

    @org.junit.Test
    public void set_getCart_ID() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCart_ID(1);
        assertEquals(1, cart.getCart_ID());
    }

    @org.junit.Test
    public void set_getCustomer() {
        User customer = new User();
        ShoppingCart cart = new ShoppingCart();
        customer.setCart(cart);
        cart.setCustomer(customer);
        assertEquals(customer.getUser_ID(), cart.getCustomer().getUser_ID());
    }

    @org.junit.Test
    public void set_getBooks() {
        ShoppingCart cart = new ShoppingCart();
        List<Book> books = new ArrayList<>();
        cart.setBooks(books);
        assertEquals(books, cart.getBooks());
    }

    @org.junit.Test
    public void checkout() {
    }
}