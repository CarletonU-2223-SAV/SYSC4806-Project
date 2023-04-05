package com.groupseven.sysc4806project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderHistoryTest {

    @Test
    public void set_getId() {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setId(3);
        assertEquals(3, orderHistory.getId());
    }

    @Test
    public void set_getPastOrders() {
        OrderHistory orderHistory = new OrderHistory();
        List<ShoppingCart> orders = new ArrayList<>();
        orderHistory.setPastOrders(orders);
        ShoppingCart cart = new ShoppingCart();
        orders.add(cart);
        orderHistory.addOrder(cart);
        assertEquals(orders, orderHistory.getPastOrders());
    }
}
