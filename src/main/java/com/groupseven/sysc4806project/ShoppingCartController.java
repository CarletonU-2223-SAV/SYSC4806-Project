package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, BookService bookService){
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String cart(@RequestParam Integer cartId,
                       Model model){
        Map<Integer, Integer> bookIds = shoppingCartService.listBooksInCart(cartId);
        Map<Book, Integer> books = new HashMap<>();
        for (int id : bookIds.keySet()){
            books.put(bookService.get(id), bookIds.get(id));
        }
        model.addAttribute("books", books);
        return "cart-page";
    }

    public String addBook(@RequestParam Integer cartId,
                          @RequestParam Integer bookId){
        shoppingCartService.addBookToCart(cartId, bookId);
        return "redirect:/cart";
    }

    public String removeBook(@RequestParam Integer cartId,
                             @RequestParam Integer bookId){
        shoppingCartService.removeBookFromCart(cartId, bookId);
        return "redirect:/cart";
    }

    public String clearCart(@RequestParam Integer cartId){
        shoppingCartService.clearCart(cartId);
        return "redirect:/cart";
    }

    public String changeOrderAmount(@RequestParam Integer cartId,
                                    @RequestParam Integer bookId,
                                    @RequestParam Integer orderAmount){
        shoppingCartService.changeOrderAmount(cartId, bookId, orderAmount);
        return "redirect:/cart";
    }

}
