package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderHistoryController {

    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public OrderHistoryController(UserService userService, BookService bookService){
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public String listOrders(@CookieValue Integer userId,
                           Model model){
        User user = userService.getUser(userId);
        if (user != null){
            OrderHistory orderHistory = userService.getOrderHistory(userId);
            Map<BigDecimal, Map<Book, Integer>> allOrders = new HashMap<>();
            for (ShoppingCart cart : orderHistory.getPastOrders()){
                BigDecimal sum = BigDecimal.ZERO;
                Map<Integer, Integer> bookIds = cart.getBooks();
                Map<Book, Integer> books = new TreeMap<>(Comparator.comparing(Book::getId));
                for (Integer id : bookIds.keySet()){
                    Book book = bookService.get(id);
                    int orderAmount = bookIds.get(id);
                    books.put(book, orderAmount);
                    sum = sum.add(book.getPrice().multiply(new BigDecimal(orderAmount)));
                }
                allOrders.put(sum, books);
            }
            model.addAttribute("allOrders", allOrders);
        }
        model.addAttribute("user", user);
        return "orders";
    }

}