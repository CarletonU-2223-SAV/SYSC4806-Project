package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    private int id;
    private boolean isAdmin;
    private String name;
    private ShoppingCart cart;
    private Set<Book> purchaseHistory;

    private OrderHistory orderHistory;

    public User() {
        purchaseHistory = new HashSet<>();
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Book> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(Set<Book> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Transient
    public boolean addPurchaseHistory(Book book) {
        this.purchaseHistory.add(book);
        return true;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void addToOrderHistory(ShoppingCart cart){
        orderHistory.addOrder(cart);
    }
}
