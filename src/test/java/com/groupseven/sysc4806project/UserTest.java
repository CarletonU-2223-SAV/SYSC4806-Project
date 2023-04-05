package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class UserTest {

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
    public void set_getCart() {
        User user = new User();
        ShoppingCart cart = new ShoppingCart();
        user.setCart(cart);
        assertEquals(user.getId(), user.getCart().getId());
    }

    @Test
    public void set_get_PurchaseHistory() {
        User user = new User();
        Set<Book> purchasedBooks = new HashSet<>();
        user.setPurchaseHistory(purchasedBooks);
        assertEquals(purchasedBooks, user.getPurchaseHistory());
    }
}