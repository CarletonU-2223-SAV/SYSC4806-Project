package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class UserTest {

    @Test
    public void set_getUser_ID() {
        User user = new User();
        user.setUser_ID(4);
        assertEquals(4, user.getUser_ID());
    }

    @Test
    public void set_getName() {
        User user = new User();
        user.setName("Test");
        assertEquals("Test", user.getName());
    }

    @Test
    public void set_getCart() {
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        cart.setCustomer(user);
        user.setCart(cart);
        assertEquals(cart, user.getCart());
    }

    @Test
    public void set_get_isAdmin() {
        User user = new User();
        user.setAdmin(true);
        assertEquals(true, user.isAdmin());
    }
}