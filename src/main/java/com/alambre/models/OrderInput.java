package com.alambre.models;

import java.util.List;

public class OrderInput {
    private Integer tableNumber;
    private List<OrderItem> items;

    public OrderInput(Integer tableNumber, List<OrderItem> items) {
        this.tableNumber = tableNumber;
        this.items = items;
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
}
