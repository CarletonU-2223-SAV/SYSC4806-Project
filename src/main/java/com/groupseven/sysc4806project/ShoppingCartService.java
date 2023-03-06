package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartService {


    private final ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ShoppingCartService (ShoppingCartRepository shoppingCartRepository){
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @GetMapping("/{cartId}")
    public ShoppingCart getCart(@PathVariable int cartId){
        return shoppingCartRepository.findById(cartId).orElse(null);
    }

    @GetMapping("/create/{userId}")
    public Integer createCart(@PathVariable int userId){
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        ShoppingCart cart = new ShoppingCart();
        cart.setCustomer(user);
        shoppingCartRepository.save(cart);
        return cart.getId();
    }

    @GetMapping("/{cartId}/books")
    public List<Book> listBooksInCart(@PathVariable int cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        assert cart != null;
        return cart.getBooks();
    }

    @GetMapping("/{cartId}/{bookId}")
    public Boolean addBookToCart(@PathVariable int cartId,
                                 @PathVariable int bookId
                        ){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (cart == null || book == null){
            return false;
        }
        cart.addBook(book);
        shoppingCartRepository.save(cart);
        return true;
    }

    @DeleteMapping("/{cartId}/{bookId}")
    public Boolean removeBookFromCart(@PathVariable int cartId,
                                      @PathVariable int bookId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        cart.removeBook(bookId);
        shoppingCartRepository.save(cart);
        return true;
    }

    @PostMapping("/{cartId}/{bookId}")
    public Boolean changeOrderAmount(@PathVariable int cartId,
                                     @PathVariable int bookId,
                                     @RequestParam int orderAmount){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        Book book = cart.getBook(bookId);
        if (orderAmount > book.getInventory()){
            return false;
        }
        book.setOrderAmount(orderAmount);
        shoppingCartRepository.save(cart);
        return true;
    }

    @DeleteMapping ("/clear/{cartId}")
    public Boolean clearCart(@PathVariable int cartId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart == null){
            return false;
        }
        cart.clearCart();
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
