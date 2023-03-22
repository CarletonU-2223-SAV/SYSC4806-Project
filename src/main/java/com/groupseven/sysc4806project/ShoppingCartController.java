package com.groupseven.sysc4806project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final UserService userService;
    private final BookService bookService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String SHIPPING_API = "https://amazin.azurewebsites.net/api/calculateshipping";

    @Autowired
    public ShoppingCartController(UserService userService, BookService bookService){
        this.userService = userService;
        this.bookService = bookService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void listBooks(@CookieValue Integer userId,
                          Model model) {
        BigDecimal sum = BigDecimal.ZERO;
        Map<Integer, Integer> bookIds = userService.listBooksInCart(userId);
        Map<Book, Integer> books = new TreeMap<>(Comparator.comparing(Book::getId));
        for (Integer id : bookIds.keySet()){
            Book book = bookService.get(id);
            int orderAmount = bookIds.get(id);
            if (book == null){
                userService.removeBookFromCart(userId, id);
            }else{
                books.put(book, orderAmount);
                sum = sum.add(book.getPrice().multiply(new BigDecimal(orderAmount)));
            }
        }
        User user = userService.getUser(userId);
        Map<String, String> rates = null;
        if(user != null){
            model.addAttribute("user", user);

            try {
                MultiValueMap<String, String> headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                HttpEntity<String> entity = new HttpEntity<>(
                        objectMapper.writeValueAsString(userService.getUser(userId).getCart()),
                        headers
                );
                rates = restTemplate.exchange(
                        SHIPPING_API,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Map<String, String>>() {
                        }
                ).getBody();
            } catch (Exception e) {
                // Don't want to break the entire page if Azure goes down
                e.printStackTrace();
            }
        }
        model.addAttribute("rates", rates);
        model.addAttribute("books", books);
        model.addAttribute("totalPrice", sum);
    }

    @GetMapping("")
    public String cart(@CookieValue Integer userId,
                       Model model){
        listBooks(userId, model);
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
        listBooks(userId, model);
        model.addAttribute("userId", userId);
        return "checkout-page";

    }

    @PostMapping("/COH")
    public String checkout(@RequestParam Integer userId){
        userService.checkoutUser(userId);
        return "redirect:/home";
    }
}
