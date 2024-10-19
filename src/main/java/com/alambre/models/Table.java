package com.alambre.models;

import java.util.List;
import java.util.UUID;

public class Table {
    private UUID tableID;
    private boolean occupied;
    private Order currentOrder;

    public Table() {
        this.tableID = UUID.randomUUID();
        this.occupied = false;
        this.currentOrder = null;
    }

    public UUID getTableID() {
        return tableID;
    }

    public void setTableID(UUID tableID) {
        this.tableID = tableID;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}
