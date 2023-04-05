package com.groupseven.sysc4806project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderHistory {

    private List<ShoppingCart> pastOrders;

    private int id;

    public OrderHistory(){
        pastOrders = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<ShoppingCart> getPastOrders() {
        return pastOrders;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPastOrders(List<ShoppingCart> pastOrders) {
        this.pastOrders = pastOrders;
    }

    public void addOrder(ShoppingCart cart){
        pastOrders.add(cart);
    }
}
