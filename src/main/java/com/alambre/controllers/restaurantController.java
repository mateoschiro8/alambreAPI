package com.alambre.controllers;

import com.alambre.models.Order;
import com.alambre.models.OrderItem;
import com.alambre.models.OrderStatus;
import com.alambre.models.Restaurant;
import com.alambre.models.Menu;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private ConcurrentHashMap<UUID, Restaurant> restaurants = new ConcurrentHashMap<>();

    @GetMapping("/")
    public List<Restaurant> getRestaurants() {
        return List.copyOf(restaurants.values());
    }

    @PostMapping("/")
    public void addRestaurant(@RequestBody Restaurant restaurant) {
        // TODO ver si hay que generar el uuid aca
        restaurants.put(restaurantId, newRestaurant);
    }

    @GetMapping("/{restaurantID}")
    public Restaurant getRestaurant(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).orElse(null);
    }

    @GetMapping("/{restaurantID}/menu")
    public Menu getMenu(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getMenu).orElse(null);
    }

    @GetMapping("/{restaurantID}/orders")
    public List<Order> getRestaurantOrders(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getOrders).orElse(null);
    }

    @GetMapping("/{restaurantID}/orders/{orderID}")
    public Order getOrder(@PathVariable UUID restaurantID, @PathVariable UUID orderID) {
        return findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst())
                .orElse(null);
    }

    @PatchMapping("/restaurants/{restaurantID}/orders/{orderID}")
    public void updateOrderStatus(@PathVariable UUID restaurantID, @PathVariable UUID orderID, @RequestBody OrderStatus newStatus) {
        Optional<Order> orderOptional = findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.updateStatus(newStatus);
        } else {
            throw new IllegalStateException("Order not found");
        }
    }

    private Optional<Restaurant> findRestaurantById(UUID restaurantID) {
        return Optional.ofNullable(restaurants.get(restaurantID));
    }
}
