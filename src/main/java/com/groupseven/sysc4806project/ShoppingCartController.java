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

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, BookService bookService){
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String cart(@CookieValue Integer cartId,
                       Model model){
        Map<Integer, Integer> bookIds = shoppingCartService.listBooksInCart(cartId);
        Map<Book, Integer> books = new TreeMap<>(Comparator.comparing(Book::getTitle));
        for (Integer id : bookIds.keySet()){
            if (bookService.get(id) == null){
                shoppingCartService.removeBookFromCart(cartId, id);
            }else{
                books.put(bookService.get(id), bookIds.get(id));
            }
        }
        model.addAttribute("books", books);
        model.addAttribute("cartId", cartId);
        return "cart-page";
    }

    @PostMapping("/add")
    public String addBook(@CookieValue Integer cartId,
                          @RequestParam Integer bookId){
        shoppingCartService.addBookToCart(cartId, bookId);
        return "redirect:/cart";
    }

    @PostMapping ("/remove")
    public String removeBook(@RequestParam Integer cartId,
                             @RequestParam Integer bookId){
        shoppingCartService.removeBookFromCart(cartId, bookId);
        return "redirect:/cart";
    }

    @PostMapping ("/clear")
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
