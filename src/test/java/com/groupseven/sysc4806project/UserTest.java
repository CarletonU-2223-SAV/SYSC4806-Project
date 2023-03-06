package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;

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

    @Test
    public void testGetUser(){
        User user = new User();
        userRepository.save(user);

        ResponseEntity<User> response = restTemplate.getForEntity(
                "/api/user" + user.getUser_ID(),
                User.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUser(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", "Kevin Smith");
        params.add("isAdmin", "true");
        ResponseEntity<Integer> response = restTemplate.postForEntity(
                "/api/user",
                params,
                Integer.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);

        User user = userRepository.findById(response.getBody()).orElseThrow();
        assertEquals("Kevin Smith", user.getName());
        assertTrue(user.isAdmin());
    }
}