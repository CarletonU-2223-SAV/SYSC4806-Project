package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookService {
    private final BookRepository repo;

    @Autowired
    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping("")
    public List<Book> list() {
        List<Book> books = new ArrayList<>();
        this.repo.findAll().iterator().forEachRemaining(books::add);
        return books;
    }

    @GetMapping("/{bookId}")
    public Book get(@PathVariable Integer bookId) {
        return this.repo.findById(bookId).orElse(null);
    }

    @PostMapping("")
    public Integer create(
            @RequestParam String isbn,
            @RequestParam String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam String author,
            @RequestParam String publisher,
            @RequestParam(defaultValue = "0") Integer inventory,
            @RequestParam(required = false) MultipartFile image
    ) {
        Book newBook = new Book();
        newBook.setIsbn(isbn);
        newBook.setTitle(title);
        newBook.setDescription(description);
        newBook.setAuthor(author);
        newBook.setPublisher(publisher);
        newBook.setInventory(inventory);

        this.repo.save(newBook);

        if (image != null) {
            newBook.setImage(image);
        }

        return newBook.getId();
    }

    @PostMapping("/{bookId}")
    public Boolean update(
        @PathVariable Integer bookId,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String publisher,
        @RequestParam(required = false) Integer inventory,
        @RequestParam(required = false) MultipartFile image
    ) {
        Optional<Book> bookOptional = this.repo.findById(bookId);
        if (bookOptional.isEmpty()) {
            // Could not find book with given ID
            return false;
        }

        Book book = bookOptional.get();

        if (isbn != null) {
            book.setIsbn(isbn);
        }

        if (title != null) {
            book.setTitle(title);
        }

        if (description != null) {
            book.setDescription(description);
        }

        if (author != null) {
            book.setAuthor(author);
        }

        if (publisher != null) {
            book.setPublisher(publisher);
        }

        if (inventory != null) {
            book.setInventory(inventory);
        }

        this.repo.save(book);

        if (image != null) {
            return book.setImage(image);
        }

        return true;
    }

    @DeleteMapping("/{bookId}")
    public Boolean delete(@PathVariable Integer bookId) {
        Optional<Book> bookOptional = this.repo.findById(bookId);
        if (bookOptional.isEmpty()) {
            // Could not find book with given ID
            return false;
        }

        Book b = bookOptional.get();

        // Free up image file
        b.removeImage();

        this.repo.delete(bookOptional.get());
        return true;
    }
}
