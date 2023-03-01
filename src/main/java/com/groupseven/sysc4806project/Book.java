package com.groupseven.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.awt.*;
import java.io.Serializable;
@Entity
public class Book implements Serializable {
    private Long id;
    private Image picture;
    private int inventory;
    private String isbn, description, title, author, publisher;

    public Book() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
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
    public Long getId() {
        return id;
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
