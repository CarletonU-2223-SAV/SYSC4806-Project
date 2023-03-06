package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class ShoppingCart {

    private int id;
    private User customer;
    private List<Book> books;

    public ShoppingCart() {
        books = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }
    @OneToOne
    public User getCustomer() {
        return customer;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Book> getBooks() {
        return books;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book){
        if (book != null){
            this.books.add(book);
        }
    }

    public void removeBook(int id){
        this.books.removeIf(book -> book.getId() == id);
    }

    public Book getBook(int id){
        for (Book book: books){
            if (book.getId() == id){
                return book;
            }
        }
        return null;
    }

    public void clearCart(){
        books.clear();
    }

    public void checkout() {
    }
}
