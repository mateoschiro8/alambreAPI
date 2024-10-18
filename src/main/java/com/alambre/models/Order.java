package com.alambre.models;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private List<OrderItem> items;
    private OrderStatus status;
    private UUID restaurantId;
    private UUID clientId;  

    public Order(UUID restaurantId, UUID clientId, List<OrderItem> items) {
        this.id = UUID.randomUUID();
        this.restaurantId = restaurantId;
        this.clientId = clientId;
        this.items = items;
        this.status = OrderStatus.CREATED;
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
