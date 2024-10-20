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
    // TODO cambiar a que table y customer sean polimorficos
    private List<Table> tables;
    private List<OnlineCustomer> onlineCustomers;

    // TODO agrgegar info del local (horarios, datos de contacto, fotos/logo)

    public Restaurant(RestaurantInput input) {
        this.id = UUID.randomUUID();
        this.name = input.getName();
        this.location = input.getLocation();
        this.menu = input.getMenu();
        this.orders = new ArrayList<>();

        List<Table> tables = new ArrayList<>();
        for (int i = 0; i < input.getNumberOfTables(); i++) {
            tables.add(new Table());
        }
        this.tables = tables;

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
