package com.groupseven.sysc4806project;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    private int user_ID;
    private boolean isAdmin;
    private String name;
    private ShoppingCart cart;

    public User() {
    }

    @Id
    @GeneratedValue
    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
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

    @OneToOne
    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
