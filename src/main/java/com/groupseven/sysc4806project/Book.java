package com.groupseven.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Entity
public class Book {
    private static final String IMAGE_DIR = "resources/static/";

    private int id;
    private int inventory;

    private String isbn, description, title, author, publisher;

    public Book() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    @Transient
    public String getImage() {
        return "/" + id + ".png";
    }

    @Transient
    public boolean setImage(MultipartFile image) {
        Path savePath = Paths.get(IMAGE_DIR + id + ".png");

        if (!Files.exists(savePath)) {
            try {
                Files.createDirectories(savePath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try (InputStream is = image.getInputStream()) {
            Files.copy(is, savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int getInventory() {
        return inventory;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

}