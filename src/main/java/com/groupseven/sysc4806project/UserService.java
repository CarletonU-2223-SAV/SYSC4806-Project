package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId){
        return userRepository.findById(userId).orElse(null);
    }

    @PostMapping("")
    public Integer createUser(@RequestParam String name,
                              @RequestParam Boolean isAdmin){
        User user = new User();
        user.setName(name);
        user.setAdmin(isAdmin);
        userRepository.save(user);
        return user.getId();
    }
}
