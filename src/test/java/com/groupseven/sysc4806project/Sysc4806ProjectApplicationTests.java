package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class Sysc4806ProjectApplicationTests {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookstoreController bookstoreController;

	@Autowired
	private ImageController imageController;

	@Autowired
	private ImageService imageService;

	@Test
	void contextLoads() {
		assertNotNull(bookRepository);
		assertNotNull(bookService);
		assertNotNull(bookstoreController);
		assertNotNull(imageController);
		assertNotNull(imageService);
	}

}
