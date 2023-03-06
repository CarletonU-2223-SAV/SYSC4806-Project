package com.groupseven.sysc4806project;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class BookstoreController {
    private final BookService bookService;

    @Autowired
    public BookstoreController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public String home(
            @RequestParam(defaultValue = "") String isbn,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "") String publisher,
            Model model
    ) {
        List<Book> books = this.bookService.list().stream()
            .filter(book -> (
                (isbn.equals("") || book.getIsbn().equals(isbn))
                && (title.equals("") || StringUtils.containsIgnoreCase(book.getTitle(), title))
                && (author.equals("") || StringUtils.containsIgnoreCase(book.getAuthor(), author))
                && (publisher.equals("") || StringUtils.containsIgnoreCase(book.getPublisher(), publisher))
            )).collect(Collectors.toList());
        model.addAttribute("books", books);
        return "index";
    }

    @PostMapping("/add-book")
    public String add(
            @RequestParam(defaultValue = "") String isbn,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "") String publisher,
            @RequestParam(defaultValue = "") Integer inventory,
            @RequestParam(required = false) MultipartFile image,
            Model model
    ) {
        int book_id = this.bookService.create(isbn,title,description,author,publisher,inventory,image);
        Optional<Book> added_book = Optional.ofNullable(this.bookService.get(book_id));
        Book book = added_book.get();
        model.addAttribute("book", book);
        return "add-edit";
    }
}

