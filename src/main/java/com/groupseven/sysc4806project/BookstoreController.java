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
            @RequestParam(required = false) MultipartFile image
    ) {
        if (inventory < 0) {
            inventory = 0;
        }
        this.bookService.create(isbn,title,description,author,publisher,inventory,image);
        return "redirect:/home";
    }

    @PostMapping("/edit-book")
    public String edit(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Integer inventory,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam Integer book_id
    ) {
        if (inventory < 0) {
            inventory = 0;
        }
        this.bookService.update(book_id,isbn,title,description,author,publisher,inventory,image);
        return "redirect:/home";
    }

    @GetMapping("/transit-to-add-book")
    public String transit_add() {
        return "add-edit";
    }

    @GetMapping("/transit-to-edit-book")
    public String transit_edit(@RequestParam int id, Model model) {
        Optional<Book> chosen_book = Optional.ofNullable(this.bookService.get(id));
        Book book = chosen_book.get();
        model.addAttribute("book", book);
        return "add-edit";
    }
}

