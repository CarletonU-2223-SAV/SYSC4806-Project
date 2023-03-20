package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserService {

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository){
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /*public Integer getLowestID(){
        User user = null;
        for (User currentUser : userRepository.findAll()){
            if (user == null || currentUser.getId() < user.getId()){
                user = currentUser;
            }
        }
        if (user == null){
            return createUser("Adam", true);
        }
        return user.getId();
    }*/

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId){
        return userRepository.findById(userId).orElse(null);
    }

    @PostMapping("")
    public Integer createUser(@RequestParam String name,
                              @RequestParam Boolean isAdmin){
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        user.setName(name);
        user.setAdmin(isAdmin);
        user.setCart(shoppingCart);
        userRepository.save(user);
        return user.getId();
    }

    @GetMapping("/{userId}/books")
    public Map<Integer, Integer> listBooksInCart(@PathVariable Integer userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            //user not in repo
            return null;
        }
        return user.getCart().getBooks();
    }

    @GetMapping("/{userId}/{bookId}")
    public Boolean addBookToCart(@PathVariable Integer userId,
                                 @PathVariable Integer bookId
    ){
        User user = userRepository.findById(userId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (user == null || book == null){
            //user or book not in repo
            return false;
        }
        if (user.getCart().addBookID(bookId)){
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @DeleteMapping("/{userId}/{bookId}")
    public Boolean removeBookFromCart(@PathVariable Integer userId,
                                      @PathVariable Integer bookId){
        User user = userRepository.findById(userId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (user == null || book == null){
            //user or book not in repo
            return false;
        }
        if (user.getCart().removeBookID(bookId)){
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @PostMapping("/{userId}/{bookId}")
    public Boolean changeOrderAmount(@PathVariable Integer userId,
                                     @PathVariable Integer bookId,
                                     @RequestParam Integer orderAmount){
        User user = userRepository.findById(userId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (user == null || book == null || orderAmount > book.getInventory()){
            return false;
        }
        if (user.getCart().setOrderAmount(orderAmount, bookId)){
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @DeleteMapping ("/clear/{userId}")
    public Boolean clearCart(@PathVariable Integer userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            return false;
        }
        user.getCart().clear();
        userRepository.save(user);
        return true;
    }

    @DeleteMapping ("/{userId}")
    public Boolean deleteUser(@PathVariable Integer userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @GetMapping("/{userName}")
    public User getUserName(@PathVariable String userName) {
        return userRepository.findUserByName(userName).orElse(null);
    }

}
