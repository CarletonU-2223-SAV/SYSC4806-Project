package com.groupseven.sysc4806project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class BookstoreController {
    @GetMapping("")
    public String home(Model model) {
        List<Book> books = List.of(new Book(
                "12345",
                "https://upload.wikimedia.org/wikipedia/commons/b/b6/Gutenberg_Bible%2C_Lenox_Copy%2C_New_York_Public_Library%2C_2009._Pic_01.jpg",
                "A book from Wikipedia",
                "Demo",
                "Some Author",
                "Publishing House",
                "999"
        ));
        model.addAttribute("books", books);
        return "index";
    }
}
