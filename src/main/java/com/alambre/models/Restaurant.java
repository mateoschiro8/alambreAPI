package com.alambre.models;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {

    private Integer id;

    private String name;
    private String number;
    private Coordinate location;
    private String logoUrl;
    private List<String> images;
    private String openingTime;
    private String closingTime;

    private List<MenuItem> menu;
    private List<Order> orders;
    private List<String> qrs;

    private HashMap <Integer,Integer> tableOrders;

    public Restaurant(Integer restaurantID, RestaurantInput input) {
        this.id = restaurantID;

        this.name = input.getName();
        this.number = input.getNumber();
        this.location = input.getLocation();
        this.logoUrl = input.getLogoUrl();
        this.images = input.getImages();
        this.menu = input.getMenu();
        this.tableOrders = new HashMap<>();
        for(int i = 1; i <= input.getNumberOfTables(); i++) {
            this.tableOrders.put(i, 0);
            // this.generateTableQR(i);
        }
        this.openingTime = input.getOpeningTime();
        this.closingTime = input.getClosingTime();

        this.orders = new ArrayList<>();


        this.qrs = new ArrayList<>();

    }

    public BufferedImage generateTableQR(Integer tableID) {
        String frontBaseURL = "https://p11-p11.github.io/front_atado_con_alambre";
        
        String url = String.format("%s/user/order/%d?table=%d", frontBaseURL, this.getID(), tableID);

        return QRGenerator.getInstance().generateQRCodeImage(url);

        /* String fileName = this.getName() + "/table_" + tableID;
        if (QRGenerator.getInstance().saveQRCodeImage(url, fileName)) {
            this.qrs.add("/qrcodes/" + fileName + ".png");
        }
         */
    }

    public ResponseEntity<Map<String, Integer>> addOrder(OrderInput input) {
        Integer tableNumber = input.getTableNumber();

        Map<String, Integer> responseBody = new HashMap<>();

        if (tableNumber != 0) {
            if (input.getTableNumber() > this.tableOrders.size() || this.tableOrders.get(tableNumber) != 0 || !this.isNear(input.getUserLocation())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
        }

        Integer orderID = this.orders.size() + 1;

        Order order = new Order(input, orderID);
        this.orders.add(order);
        
        if (tableNumber != 0) {
            this.tableOrders.put(tableNumber, order.getId());
        }

        responseBody.put("id", order.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    public void emptyTable(Integer tableID) {
        this.tableOrders.put(tableID, 0);
    }

    public Order getOrderById(Integer orderID) {
        return this.orders.stream()
                .filter(order -> order.getId().equals(orderID))
                .findFirst()
                .orElse(null);
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = this.getOrderById(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
        }
    }

    public boolean isNear(Coordinate coordinate) {
        return location.calculateDistanceTo(coordinate.getLatitude(), coordinate.getLongitude()) <= 50;
    }

    public Integer getID() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

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

    public List<String> getQRs() { return qrs; }
}
