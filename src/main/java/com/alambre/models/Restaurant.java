package com.alambre.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant {

    private UUID id;
    private String name;
    private Coordinate location;
    private Menu menu;
    private List<Order> orders;

    public Restaurant(int id, String name, Coordinate location, Menu menu) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.orders = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public Coordinate getLocation() {
        return location;
    }
    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Order getOrderById(UUID orderId) {
        return this.orders.stream()
            .filter(order -> order.getId().equals(orderId))
            .findFirst()
            .orElse(null);
    }

    public void updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = this.getOrderById(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
        }
    }


}
