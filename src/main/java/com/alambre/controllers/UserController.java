package com.alambre.controllers;

import com.alambre.models.Order;
import com.alambre.models.OrderItem;
import com.alambre.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    @GetMapping("/{userId}/orders")
    public List<Order> getUserOrders(@PathVariable UUID userId) {
        User user = users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user.getOrders();
    }

    @PostMapping("/{userID}/orders")
    public void createOrder(@PathVariable UUID userID, @RequestBody UUID restaurantID, @RequestBody List<OrderItem> orderItems) {
        Order order = new Order(restaurantID, userID, orderItems);
        restaurant = findRestaurantById(restaurantID)

        getUserById(userID).ifPresent(user -> user.addOrder(order));
    }

    @PostMapping("/")
    public User createUser() {
        User newUser = new User();
        users.put(newUser.getUserId(), newUser);
        return newUser;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
