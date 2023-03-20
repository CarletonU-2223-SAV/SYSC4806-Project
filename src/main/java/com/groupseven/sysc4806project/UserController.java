package com.groupseven.sysc4806project;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sign-up")
    public String signup() {
        return "sign-up";
    }

    @GetMapping("/get-user")
    public String getUser(@RequestParam String username, Model model, HttpServletResponse response){
        User user = userService.getUserName(username);
        model.addAttribute("User", user);
        Cookie springCookie = new Cookie("userId", String.valueOf(user.getId()));
        System.out.println(springCookie);
        response.addCookie(springCookie);
        return "redirect:/home";
    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String username,
                             @RequestParam(required = false) Boolean isAdmin) {
        if(isAdmin == null){
            isAdmin = false;
        }
        userService.createUser(username, isAdmin);
        return"redirect:/home";
    }
}
