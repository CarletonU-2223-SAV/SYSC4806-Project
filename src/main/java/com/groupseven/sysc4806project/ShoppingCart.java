package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class ShoppingCart {

    private int cart_ID;
    private User customer;
    private List<Book> books;

    public ShoppingCart() {
        books = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    public int getCart_ID() {
        return cart_ID;
    }
    @OneToOne
    public User getCustomer() {
        return customer;
    }

    @OneToMany
    public List<Book> getBooks() {
        return books;
    }

    public void setCart_ID(int cart_ID) {
        this.cart_ID = cart_ID;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void checkout() {

    }
}
