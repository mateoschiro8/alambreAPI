package com.alambre.models;

import java.rmi.server.UID;
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
    private String openingTime;
    private String closingTime;

    private List<MenuItem> menu;
    private List<Order> orders;

    private Integer maxTables;
    private HashMap <Integer,UUID> tables;

    private UUID emptyTableUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    // TODO agrgegar info del local (horarios)
    public Restaurant(RestaurantInput input) {
        this.id = UUID.randomUUID();

        this.name = input.getName();
        this.number = input.getNumber();
        this.location = input.getLocation();
        this.images = input.getImages();
        this.menu = input.getMenu();
        this.openingTime = input.getOpeningTime();
        this.closingTime = input.getClosingTime();

        this.orders = new ArrayList<>();

        this.maxTables = input.getNumberOfTables();
        this.tables = new HashMap<>();

        for(int i = 1; i <= this.maxTables; i++) {
            this.tables.put(i, emptyTableUUID);
        }
    }

    public boolean addOrder(OrderInput input) {

        // TODO mesa 0

        if(!(this.tables.get(input.getTableNumber()) == this.emptyTableUUID) || input.getTableNumber() > this.maxTables)
            return false;

        Order order = new Order(input);
        this.orders.add(order);
        this.tables.put(input.getTableNumber(), order.getId());
        return true;
    }

    public void emptyTable(UUID orderID) {
        
        Order orderToUpdate;
        for(int i = 0; i < this.orders.size(); i++) {
            if(this.orders.get(i).getId() == orderID)
                orderToUpdate = this.orders.get(i);
        }   
        
        this.tables.put(orderToUpdate.getTableNumber(), this.emptyTableUUID);
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

    public String getOpeningTime() { return openingTime; }

    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }

    public String getClosingTime() { return closingTime; }

    public void setClosingTime(String closingTime) { this.closingTime = closingTime; }

}
