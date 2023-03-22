package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    private int id;
    private boolean isAdmin;
    private String name;
    private ShoppingCart cart;
    private Set<Book> purchaseHistory;

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
    public String getMostCommonGenre() {
        Set<Book> books = this.getPurchaseHistory();
        //initiate the dictionary containing the book's genre and their counts
        Map<String, Long> dictionary = books.stream().collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
        //get most common genre if it exists, null otherwise
        String result_genre = dictionary.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
        return result_genre;
    }

    @Transient
    public boolean addPurchaseHistory(Book book) {
        this.purchaseHistory.add(book);
        return true;
    }
}
