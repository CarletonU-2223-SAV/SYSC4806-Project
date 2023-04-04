package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookstoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testHomePageNoBooks() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no books to display")));
    }

    @Test
    public void testHomePageWithBook() throws Exception {
        Book b = new Book();
        b.setTitle("My Book Title");
        b.setIsbn("98765");
        b.setAuthor("Agatha Christie");
        b.setPublisher("Forgot");
        b.setGenre("Mystery");
        bookRepository.save(b);
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("My Book Title")));
    }

    @Test
    public void testFilter() throws Exception {
        Book b = new Book();
        b.setTitle("My Book 2");
        b.setIsbn("55555");
        b.setAuthor("Arthur");
        b.setPublisher("PubCo");
        b.setGenre("Dark");
        bookRepository.save(b);
        this.mockMvc.perform(get("/home?title=My Book 2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Arthur")));

        this.mockMvc.perform(get("/home?title=Other Book&publisher=PubCo&genre=Dark"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There are no books to display")));

        this.mockMvc.perform(get("/home?isbn=55555"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("My Book 2")));
    }

    @Test
    public void testAddBookPage() throws Exception {
        this.mockMvc.perform(
                post("/home/add-book")
                        .param("isbn", "994466")
                        .param("title", "Test title")
                        .param("description", "Test description")
                        .param("author", "Test author")
                        .param("publisher", "Test publisher")
                        .param("genre", "action")
                        .param("inventory", "4")
        ).andExpect(status().isFound());

        // Check that new book appears on home page
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("994466")));
    }

    @Test
    public void testEditBookPage() throws Exception {
        Book test = new Book();
        test.setTitle("Test");
        test.setInventory(5);
        test.setDescription("Test description");
        test.setAuthor("Test author");
        test.setIsbn("Test");
        test.setPublisher("Test publisher");
        test.setGenre("Test genre");
        bookRepository.save(test);

        this.mockMvc.perform(
                post("/home/edit-book")
                        .param("description", "ABABABA")
                        .param("inventory", "777")
                        .param("book_id", String.valueOf(test.getId()))
        ).andExpect(status().isFound());

        // Check that new book details appear on home page
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("777")))
                .andExpect(content().string(containsString("ABABABA")));
    }
}
