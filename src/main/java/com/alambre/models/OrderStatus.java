package com.alambre.models;

public enum OrderStatus {
    CREATED,
    ACCEPTED,
    REJECTED,
    IN_PREPARATION,
    READY_FOR_PICKUP,
    READY_TO_BE_SERVED,
    DELIVERED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case CREATED:
                return newStatus == ACCEPTED || newStatus == REJECTED;
            case ACCEPTED:
                return newStatus == IN_PREPARATION;
            case IN_PREPARATION:
                return newStatus == READY_FOR_PICKUP || newStatus == READY_TO_BE_SERVED;
            case READY_FOR_PICKUP:
                return newStatus == DELIVERED;
            case READY_TO_BE_SERVED:
                return newStatus == DELIVERED; 
            default:
                return false;
        }
    }

    public boolean shouldNotify() {
        return this == READY_FOR_PICKUP;
    }
}
