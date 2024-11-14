package com.alambre.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Restaurant {

    private final Integer id;

    private String name;
    private String phoneNumber;
    private Coordinate location;
    private String logoUrl;
    private List<String> images;
    private String openingTime;
    private String closingTime;

    private List<MenuItem> menu;
    private List<Order> orders;
    private List<String> qrs;

    private HashMap <Integer, Optional<Order>> tableOrders;

    public Restaurant(Integer restaurantID, RestaurantInput input) {
        this.id = restaurantID;

        this.name = input.getName();
        this.phoneNumber = input.getNumber();
        this.location = input.getLocation();
        this.logoUrl = input.getLogoUrl();
        this.images = input.getImages();
        this.menu = input.getMenu();
        this.openingTime = input.getOpeningTime();
        this.closingTime = input.getClosingTime();

        this.orders = new ArrayList<>();

        this.tableOrders = new HashMap<>();
        this.qrs = new ArrayList<>();

        for(int i = 1; i <= input.getNumberOfTables(); i++) {
              this.tableOrders.put(i, Optional.empty());
              this.generateTableQR(i);
        }
    }

    public void generateTableQR(Integer tableID) {
        String frontBaseURL = "urlFront";
        
        String url = String.format("%s/user/order/%d?table=&d", frontBaseURL, this.getID(), tableID);

        String fileName = this.name + "/table_" + tableID;
        if (QRGenerator.getInstance().saveQRCodeImage(url, fileName)) {
            this.qrs.add("/qrcodes/" + fileName + ".png");
        }
    }


    public Order addOrder(OrderInput input) throws IllegalArgumentException {
        Integer tableNumber = input.getTableNumber();

        validateOrderInput(input, tableNumber);

        Integer orderID = this.orders.size() + 1;
        Order order = new Order(input, orderID);
       
        this.orders.add(order);

        this.tableOrders.put(input.getTableNumber(), Optional.of(order));
        
        return order;
    }

    private void validateOrderInput(OrderInput input, Integer tableNumber) {
        if (tableNumber > this.tableOrders.size() || this.tableOrders.get(tableNumber).isPresent() || !isNear(input.getUserLocation())) {
            throw new IllegalArgumentException("Invalid table number or location");
        };
    }

    public void emptyTable(Integer tableID) {
        this.tableOrders.put(tableID, Optional.empty());
    }

    public Optional<Order> getOrderById(Integer orderID) {
        return this.orders.stream()
                .filter(order -> order.getId().equals(orderID))
                .findFirst();
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        getOrderById(orderId).ifPresent(order -> order.updateStatus(newStatus));
    }

    final int maxDistanceInMeters = 50;

    public boolean isNear(Coordinate coordinate) {
        return location.calculateDistanceTo(coordinate.getLatitude(), coordinate.getLongitude()) <= maxDistanceInMeters;
    }

    public Integer getID() {
        return id;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<String> getQRs() { return qrs; }
}
