package com.alambre.models;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Order {
    private UUID id;
    private List<OrderItem> items;
    private OrderStatus status;  
    // TODO cambiar a que customer y table sean polimorficos, o dejar un solo id y dejar un OrderType (delivery o presencial)
    private Optional<UUID> customerID;
    private Optional<UUID> tableID;  

    public Order(Optional<UUID> customerID, Optional<UUID> tableID, List<OrderItem> items) {
        this.id = UUID.randomUUID();
        this.items = items;
        this.status = OrderStatus.CREATED;
        this.customerID = customerID;
        this.tableID = tableID;        
    }

    public UUID getId() {
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

    public void updateStatus(OrderStatus newStatus) {
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus;
            if (this.status.shouldNotify()) {
                // TODO notify
            }
        } else {
            throw new IllegalStateException("Invalid state transition from " + this.status + " to " + newStatus);
        }
    }
}
