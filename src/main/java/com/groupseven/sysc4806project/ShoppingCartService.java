package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartService {


    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShoppingCartService (ShoppingCartRepository shoppingCartRepository, BookRepository bookRepository, UserRepository userRepository){
        this.shoppingCartRepository = shoppingCartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{cartId}")
    public ShoppingCart getCart(@PathVariable int cartId){
        return shoppingCartRepository.findById(cartId).orElse(null);
    }

    @GetMapping("/create/{userId}")
    public Integer createCart(@PathVariable int userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            //user not in repo
            return null;
        }
        ShoppingCart cart = shoppingCartRepository.findByCustomer(user);
        if (cart == null){
            //user doesn't already have a cart, create new one
            cart = new ShoppingCart();
            cart.setCustomer(user);
            shoppingCartRepository.save(cart);
        }
        return cart.getId();
    }

    @GetMapping("/{cartId}/books")
    public Map<Integer, Integer> listBooksInCart(@PathVariable int cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            //cart not in repo
            return null;
        }
        return cart.getBooks();
    }

    @GetMapping("/{cartId}/{bookId}")
    public Boolean addBookToCart(@PathVariable int cartId,
                                 @PathVariable int bookId
                        ){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (cart == null || book == null){
            //cart or book not in repo
            return false;
        }
        if (cart.addBookID(bookId)){
            shoppingCartRepository.save(cart);
            return true;
        }else{
            return false;
        }

    }

    @DeleteMapping("/{cartId}/{bookId}")
    public Boolean removeBookFromCart(@PathVariable int cartId,
                                      @PathVariable int bookId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (cart == null || book == null){
            //cart or book not in repo
            return false;
        }
        if (cart.removeBookID(bookId)){
            shoppingCartRepository.save(cart);
            return true;
        }else{
            return false;
        }
    }

    @PostMapping("/{cartId}/{bookId}")
    public Boolean changeOrderAmount(@PathVariable int cartId,
                                     @PathVariable int bookId,
                                     @RequestParam int orderAmount){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (cart == null || book == null || orderAmount > book.getInventory()){
            return false;
        }
        if (cart.setOrderAmount(orderAmount, bookId)){
            shoppingCartRepository.save(cart);
            return true;
        }else{
            return false;
        }
    }

    @DeleteMapping ("/clear/{cartId}")
    public Boolean clearCart(@PathVariable int cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        cart.clear();
        shoppingCartRepository.save(cart);
        return true;
    }

    @DeleteMapping ("/{cartId}")
    public Boolean deleteCart(@PathVariable int cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        shoppingCartRepository.delete(cart);
        return true;
    }
}
