package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.*;


@Entity
public class ShoppingCart {

    private int id;
    private User customer;

    //holds <bookID, orderAmount>
    private Map<Integer, Integer> books;

    public ShoppingCart() {
        books = new HashMap<>();
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

    @ElementCollection(fetch = FetchType.EAGER)
    public Map<Integer, Integer> getBooks() {
        return books;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setBooks(Map<Integer, Integer> books) {
        this.books = books;
    }

    public Boolean addBookID(int bookId){
        if (books.containsKey(bookId)){
            return false;
        }else{
            books.put(bookId, 1);
            return true;
        }
    }

    public Boolean removeBookID(int bookId){
        if (books.containsKey(bookId)){
            books.remove(bookId);
            return true;
        }else{
            return false;
        }
    }

    public Boolean setOrderAmount(int orderAmount, int bookId){
        if (books.containsKey(bookId)){
            books.put(bookId, orderAmount);
            return true;
        }else{
            return false;
        }
    }

    public Integer getOrderAmount(int bookId){
        if(books.containsKey(bookId)){
            return books.get(bookId);
        }
        return -1;
    }

    public void clear(){
        books.clear();
    }

    public void checkout() {
    }
}
