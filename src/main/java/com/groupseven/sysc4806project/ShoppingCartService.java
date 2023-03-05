package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/shoppingCart")
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private BookRepository bookRepository;

    public ShoppingCartService (ShoppingCartRepository shoppingCartRepository){
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @GetMapping("")
    public List<Book> list(){
        List<Book> bookList = new ArrayList<>();
        this.shoppingCartRepository.findAll().iterator().forEachRemaining(bookList::add);
        return bookList;
    }

    @RequestMapping("/{bookId}")
    public Book addBook(@PathVariable int bookId){
        Book book = bookRepository.findById(bookId).orElse(null);
        assert book != null;
        shoppingCartRepository.save(book);
        return book;
    }

    @DeleteMapping("/{bookId}")
    public Book removeBook(@PathVariable int bookId){
        Book book = shoppingCartRepository.findById(bookId).orElse(null);
        assert book != null;
        shoppingCartRepository.delete(book);
        return book;
    }

    @PostMapping("/{bookId}")
    public Book changeBookInv(@PathVariable int bookId,
                              @RequestParam int newInvAmount){
        Book book = shoppingCartRepository.findById(bookId).orElse(null);
        assert book != null;
        book.setInventory(newInvAmount);
        shoppingCartRepository.save(book);
        return book;
    }

    @PostMapping("")
    public Boolean clearCart(){
        shoppingCartRepository.deleteAll();
        return true;
    }
}
