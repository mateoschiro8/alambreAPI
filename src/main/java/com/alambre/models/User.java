package com.alambre.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userId;   
    private List<Order> orders;

    public User() {
        this.userId = UUID.randomUUID();
        this.orders = new ArrayList<>();
    }

    public Order createOrder(Restaurant restaurant, List<OrderItem> items) {
        Order newOrder = new Order(restaurant.getId(), this.userId, items);
        this.orders.add(newOrder);
        restaurant.addOrder(newOrder);
        return newOrder;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public Order getOrderById(UUID orderId) {
        return this.orders.stream()
            .filter(order -> order.getId().equals(orderId))
            .findFirst()
            .orElse(null);
    }

    public UUID getUserId() {
        return this.userId;
    }

}