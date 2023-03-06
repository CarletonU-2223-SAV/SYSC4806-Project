package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void set_get_isAdmin() {
        User user = new User();
        user.setAdmin(true);
        assertTrue(user.isAdmin());
    }
}