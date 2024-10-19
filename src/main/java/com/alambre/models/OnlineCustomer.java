package com.alambre.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnlineCustomer {
    private UUID customerID;   
    private String email;
    private Order currentOrder;

    public OnlineCustomer(String email) {
        this.customerID = UUID.randomUUID();
        this.email = email;
        this.currentOrder = null;
    }

    public UUID getCustomerID() {
        return this.customerID;
    }

    public Order getCurrentOrder() {
        return this.currentOrder;
    }

    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    public UUID getEmail() {
        return this.customerID;
    }
}