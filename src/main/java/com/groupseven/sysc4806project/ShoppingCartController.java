package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, BookService bookService, UserService userService){
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("")
    public String cart(@RequestParam(defaultValue = "0") Integer cartId,
                       @RequestParam(defaultValue = "0") Integer userId,
                       Model model){
        int newUserId = userId;
        int newCartId = cartId;
        if (userService.getUser(userId) == null){
            newUserId = userService.createUser("John Doe", true);
        }
        if (shoppingCartService.getCart(cartId) == null){
            newCartId = shoppingCartService.createCart(newUserId);
        }
        Map<Integer, Integer> bookIds = shoppingCartService.listBooksInCart(newCartId);
        Map<Book, Integer> books = new HashMap<>();
        for (int id : bookIds.keySet()){
            books.put(bookService.get(id), bookIds.get(id));
        }
        model.addAttribute("books", books);
        model.addAttribute("cartId", newCartId);
        model.addAttribute("userId", userId);
        return "cart-page";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam(defaultValue = "0") Integer cartId,
                          @RequestParam Integer bookId){
        int userId = 0;
        int newCartId = cartId;
        if (userService.getUser(userId) == null){
            userId = userService.createUser("John Doe", true);
        }
        if (shoppingCartService.getCart(cartId) == null){
            newCartId = shoppingCartService.createCart(userId);
        }
        shoppingCartService.addBookToCart(newCartId, bookId);
        return "redirect:/cart";
    }

    @DeleteMapping ("/remove")
    public String removeBook(@RequestParam Integer cartId,
                             @RequestParam Integer bookId){
        shoppingCartService.removeBookFromCart(cartId, bookId);
        return "redirect:/cart";
    }

    @DeleteMapping ("/clear")
    public String clearCart(@RequestParam Integer cartId){
        shoppingCartService.clearCart(cartId);
        return "redirect:/cart";
    }

    @PostMapping ("/changeOrdAmo")
    public String changeOrderAmount(@RequestParam Integer cartId,
                                    @RequestParam Integer bookId,
                                    @RequestParam Integer orderAmount){
        shoppingCartService.changeOrderAmount(cartId, bookId, orderAmount);
        return "redirect:/cart";
    }

}
