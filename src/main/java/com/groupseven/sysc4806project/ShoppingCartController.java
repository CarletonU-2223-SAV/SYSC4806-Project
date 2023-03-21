package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public ShoppingCartController(UserService userService, BookService bookService){
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String cart(@CookieValue Integer userId,
                       Model model){
        Map<Integer, Integer> bookIds = userService.listBooksInCart(userId);
        Map<Book, Integer> books = new TreeMap<>(Comparator.comparing(Book::getTitle));
        for (Integer id : bookIds.keySet()){
            if (bookService.get(id) == null){
                userService.removeBookFromCart(userId, id);
            }else{
                books.put(bookService.get(id), bookIds.get(id));
            }
        }
        User user = userService.getUser(userId);
        if(user != null){
            model.addAttribute("user", user);
        }
        model.addAttribute("books", books);
        model.addAttribute("userId", userId);
        return "cart-page";
    }

    @PostMapping("/add")
    public String addBook(@CookieValue Integer userId,
                          @RequestParam Integer bookId){
        userService.addBookToCart(userId, bookId);
        return "redirect:/cart";
    }

    @PostMapping ("/remove")
    public String removeBook(@RequestParam Integer userId,
                             @RequestParam Integer bookId){
        userService.removeBookFromCart(userId, bookId);
        return "redirect:/cart";
    }

    @PostMapping ("/clear")
    public String clearCart(@RequestParam Integer userId){
        userService.clearCart(userId);
        return "redirect:/cart";
    }

    @PostMapping ("/changeOrdAmo")
    public String changeOrderAmount(@RequestParam Integer userId,
                                    @RequestParam Integer bookId,
                                    @RequestParam Integer orderAmount){
        userService.changeOrderAmount(userId, bookId, orderAmount);
        return "redirect:/cart";
    }

    @GetMapping("/goToCOH")
    public String goToCheckoutPage(@RequestParam Integer userId,
                                   Model model){
        User user = userService.getUser(userId);
        Map<Integer, Integer> bookIds = userService.listBooksInCart(userId);
        Map<Book, Integer> books = new TreeMap<>(Comparator.comparing(Book::getTitle));
        for (Integer id : bookIds.keySet()){
            if (bookService.get(id) == null){
                userService.removeBookFromCart(userId, id);
            }else{
                books.put(bookService.get(id), bookIds.get(id));
            }
        }
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        return "checkout-page";

    }

    @PostMapping("/COH")
    public String checkout(@RequestParam Integer userId){
        userService.checkoutUser(userId);
        return "redirect:/home";
    }
}
