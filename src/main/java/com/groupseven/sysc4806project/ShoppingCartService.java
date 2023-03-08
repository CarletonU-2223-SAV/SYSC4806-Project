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

    public Integer getLowestID(){
        ShoppingCart shoppingCart = null;
        for (ShoppingCart currentCart : shoppingCartRepository.findAll()){
            if (shoppingCart == null || currentCart.getId() < shoppingCart.getId()){
                shoppingCart = currentCart;
            }
        }
        if (shoppingCart == null){
            User user = new User();
            userRepository.save(user);
            return createCart(user.getId());
        }
        return shoppingCart.getId();
    }

    @GetMapping("/{cartId}")
    public ShoppingCart getCart(@PathVariable Integer cartId){
        return shoppingCartRepository.findById(cartId).orElse(null);
    }

    @GetMapping("/create/{userId}")
    public Integer createCart(@PathVariable Integer userId){
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
    public Map<Integer, Integer> listBooksInCart(@PathVariable Integer cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            //cart not in repo
            return null;
        }
        return cart.getBooks();
    }

    @GetMapping("/{cartId}/{bookId}")
    public Boolean addBookToCart(@PathVariable Integer cartId,
                                 @PathVariable Integer bookId
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
    public Boolean removeBookFromCart(@PathVariable Integer cartId,
                                      @PathVariable Integer bookId){
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
    public Boolean changeOrderAmount(@PathVariable Integer cartId,
                                     @PathVariable Integer bookId,
                                     @RequestParam Integer orderAmount){
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
    public Boolean clearCart(@PathVariable Integer cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        cart.clear();
        shoppingCartRepository.save(cart);
        return true;
    }

    @DeleteMapping ("/{cartId}")
    public Boolean deleteCart(@PathVariable Integer cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        shoppingCartRepository.delete(cart);
        return true;
    }
}
