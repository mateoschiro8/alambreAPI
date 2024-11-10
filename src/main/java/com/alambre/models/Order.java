package com.alambre.models;

import java.util.List;

public class Order {
    private Integer id;
    private List<OrderItem> items;
    private OrderStatus status;  

    private Integer tableNumber;

    public Order(OrderInput input, Integer orderID) {
        this.id = orderID;
        this.items = input.getItems();
        this.status = OrderStatus.CREATED;
        this.tableNumber = input.getTableNumber();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus;
        } else {
            throw new IllegalStateException("Invalid state transition from " + this.status + " to " + newStatus);
        }
    }

    public Integer getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
}
