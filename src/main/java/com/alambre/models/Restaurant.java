package com.alambre.models;

import java.util.ArrayList;
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
    // TODO cambiar a que table y customer sean polimorficos
    private List<Table> tables;
    private List<OnlineCustomer> onlineCustomers;

    // TODO agrgegar info del local (horarios)
    public Restaurant(RestaurantInput input) {
        this.id = UUID.randomUUID();
        this.name = input.getName();
        this.number = input.getNumber();
        this.location = input.getLocation();
        this.images = input.getImages();

        this.menu = input.getMenu();
        this.orders = new ArrayList<>();

        this.tables = new ArrayList<>();
        for (int i = 0; i < input.getNumberOfTables(); i++) {
            this.tables.add(new Table());
        }

        this.onlineCustomers = new ArrayList<>();
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

    public void setName(String nombre) {
        this.name = nombre;
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

    public List<Table> getTables() {
        return this.tables;
    }

    public List<OnlineCustomer> getCustomers() {
        return this.onlineCustomers;
    }

    public void addCustomer(OnlineCustomer customer) {
        this.onlineCustomers.add(customer);
    }

    public boolean isNear(Coordinate coordinate) {
        return location.calculateDistanceTo(coordinate.getLatitude(), coordinate.getLongitude()) <= 50;
    }
}
