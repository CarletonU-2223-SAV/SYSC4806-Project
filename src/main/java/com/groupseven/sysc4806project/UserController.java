package com.groupseven.sysc4806project;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public UserController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/sign-up")
    public String signup() {
        return "sign-up";
    }

    @GetMapping("/log-out")
    public String logout(HttpServletResponse response) {
        Cookie springCookie = new Cookie("userId", null);
        springCookie.setMaxAge(0);
        springCookie.setPath("/");
        response.addCookie(springCookie);
        return "redirect:/home";
    }

    @GetMapping("/recommendation")
    public String recommendation(@CookieValue Integer userId,
                                 Model model) {
        User user = userService.getUser(userId);
        if(user == null) {
            return "redirect:/home";
        }else{
            String result_genre = user.getMostCommonGenre();
            List<Book> list_books = bookService.getBooks(result_genre);
            list_books.removeIf(book ->user.getPurchaseHistory().contains(book));
            model.addAttribute("user", user);
            model.addAttribute("books", list_books);
            return "recommendation";
        }
    }

    @GetMapping("/get-user")
    public String getUser(@RequestParam String username, HttpServletResponse response, Model model){
        User user = userService.getUserName(username);
        if (user != null) {
            Cookie springCookie = new Cookie("userId", String.valueOf(user.getId()));
            springCookie.setPath("/");
            response.addCookie(springCookie);
            return "redirect:/home";
        }else{
            model.addAttribute("user", null);
            return "login";
        }

    }

    @PostMapping("/create-user")
    public String createUser(@RequestParam String username,
                             @RequestParam(required = false) Boolean isAdmin,
                             Model model) {
        if(isAdmin == null){
            isAdmin = false;
        }
        User user = userService.getUserName(username);
        if(user!= null) {
            model.addAttribute("user", user);
            return "sign-up";
        }else{
            userService.createUser(username, isAdmin);
            return "redirect:/home";
        }
    }
}
