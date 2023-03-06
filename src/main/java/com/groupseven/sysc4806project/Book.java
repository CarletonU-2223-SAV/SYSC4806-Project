package com.groupseven.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Book {
    private int id;
    private int inventory;

    private int orderAmount;

    private String isbn, description, title, author, publisher;

    public Book() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
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
    public String getImageUrl() {
        return "/book-img/" + id;
    }

    public int getInventory() {
        return inventory;
    }

    public int getOrderAmount() {
        return orderAmount;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book b)) {
            return false;
        }

        return b.getId() == this.id;
    }
}
