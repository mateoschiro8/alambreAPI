package com.alambre.controllers;

import com.alambre.models.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restaurants")
public class RestaurantController {

    private ConcurrentHashMap<Integer, Restaurant> restaurants = new ConcurrentHashMap<>();

    @GetMapping("")
    public List<Restaurant> getRestaurants(@RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude) {

        List<Restaurant> restaurantsToReturn = List.copyOf(restaurants.values());

        if(latitude != null && longitude != null) {
            restaurantsToReturn = restaurantsToReturn.stream()
                    .filter(restaurant -> restaurant.isNear(new Coordinate(latitude, longitude)))
                    .toList();
        }

        return restaurantsToReturn;
    }

    @PostMapping("")
    public void addRestaurant(@RequestBody RestaurantInput restaurantInput) {
        Restaurant restaurant = new Restaurant(restaurants.size() + 1, restaurantInput);       
        restaurants.put(restaurant.getID(), restaurant);
    }

    @GetMapping("/{restaurantID}")
    public Restaurant getRestaurant(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID).orElse(null);
    }

    @DeleteMapping("")
    public void emptyRestaurants() {
        restaurants.clear();
    }

    // Aca habria que pegarle desde el qr, o ver de armar otro endpoint
    @GetMapping("/{restaurantID}/menu")
    public List<MenuItem> getMenu(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getMenu).orElse(null);
    }

    @GetMapping("/{restaurantID}/qrs")
    public List<String> getQRImages(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID)
                .map(Restaurant::getQRs)
                .orElse(null);
    }

    @GetMapping("/{restaurantID}/orders")
    public List<Order> getRestaurantOrders(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getOrders).orElse(null);
    }

    @PostMapping("/{restaurantID}/orders")
    public boolean addOrder(@PathVariable Integer restaurantID, @RequestBody OrderInput orderInput) {
        return findRestaurantById(restaurantID)
                .map(restaurant -> restaurant.addOrder(orderInput))
                .orElse(false);
    }
    
    @GetMapping("/{restaurantID}/orders/{orderID}")
    public Order getOrder(@PathVariable Integer restaurantID, @PathVariable Integer orderID) {
        return findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst())
                .orElse(null);
    }

    @PatchMapping("/{restaurantID}/orders/{orderID}")
    public void updateOrderStatus(@PathVariable Integer restaurantID, @PathVariable Integer orderID, @RequestBody OrderStatus newStatus) {
        
        Optional<Restaurant> resto = findRestaurantById(restaurantID);
        Optional<Order> orderOptional = findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst());

        if (orderOptional.isPresent()) {
            orderOptional.get().updateStatus(newStatus);
            
            Integer tableID = orderOptional.get().getTableNumber();

            if(newStatus == OrderStatus.DELIVERED && tableID != 0)
                resto.get().emptyTable(tableID);     
        } else {
            throw new IllegalStateException("Order not found");
        }
    }

    private Optional<Restaurant> findRestaurantById(Integer restaurantID) {
        return Optional.ofNullable(restaurants.get(restaurantID));
    }
}
