package com.alambre.models;

import java.util.List;

public class OrderInput {
    private Integer tableNumber;
    private List<OrderItem> items;
    private Coordinate userLocation;

    public OrderInput(Integer tableNumber, List<OrderItem> items, Coordinate location) {
        this.tableNumber = tableNumber;
        this.items = items;
        this.userLocation = location;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public Coordinate getUserLocation() {
        return userLocation;
    }
    public void setUserLocation(Coordinate location) {
        this.userLocation = location;
    }
}
