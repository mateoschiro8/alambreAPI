package com.alambre.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Restaurant {

    private UUID id;

    private String name;
    private String number;
    private Coordinate location;
    private List<String> images;

    private List<MenuItem> menu;
    private List<Order> orders;

    private Integer maxTables;
    private HashMap <Integer,UUID> tables;

    // TODO agrgegar info del local (horarios)
    public Restaurant(RestaurantInput input) {
        this.id = UUID.randomUUID();

        this.name = input.getName();
        this.number = input.getNumber();
        this.location = input.getLocation();
        this.images = input.getImages();
        this.menu = input.getMenu();

        this.orders = new ArrayList<>();

        this.maxTables = input.getNumberOfTables();
        this.tables = new HashMap<>();
    }

    public boolean addOrder(OrderInput input) {

        if(this.tables.containsKey(input.getTableNumber()))
            return false;

        Order order = new Order(input);
        this.orders.add(order);
        this.tables.put(input.getTableNumber(), order.getId());
        return true;
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

    public boolean isNear(Coordinate coordinate) {
        return location.calculateDistanceTo(coordinate.getLatitude(), coordinate.getLongitude()) <= 50;
    }

    public UUID getID() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public List<String> getImages() { return images; }

    public void setImages(List<String> images) { this.images = images; }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

}
