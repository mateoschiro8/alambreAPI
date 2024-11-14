package com.alambre.controllers;

import com.alambre.models.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "https://p11-p11.github.io")
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
    public ResponseEntity<Map<String, Integer>> addRestaurant(@RequestBody RestaurantInput restaurantInput) {
        Restaurant restaurant = new Restaurant(restaurants.size() + 1, restaurantInput);       
        restaurants.put(restaurant.getID(), restaurant);

        Map<String, Integer> responseBody = new HashMap<>();
        responseBody.put("id", restaurant.getID());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{restaurantID}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID);
    }

    @DeleteMapping("")
    public void emptyRestaurants() {
        File qrDirectory = new File("src/main/resources/static/qrcodes");

        // Llama a la función recursiva para borrar todo el contenido
        deleteDirectoryRecursively(qrDirectory);
        restaurants.clear();
    }

    private void deleteDirectoryRecursively(File directory) {
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    // Llamada recursiva si es un directorio
                    deleteDirectoryRecursively(file);
                }
                file.delete();  // Elimina el archivo o el directorio vacío
            }
        }
    }

    @GetMapping("/{restaurantID}/menu")
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID)
                .map(restaurant -> ResponseEntity.ok(restaurant.getMenu())); 
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
    public ResponseEntity<Order> addOrder(@PathVariable Integer restaurantID, @RequestBody OrderInput orderInput) {
        return findRestaurantById(restaurantID)
                .map(restaurant -> restaurant.addOrder(orderInput))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>()));
    }
    
    @PostMapping("/{restaurantID}/orders")
public ResponseEntity<Order> addOrder(@PathVariable Integer restaurantID, @RequestBody OrderInput orderInput) {
    return findRestaurantById(restaurantID)
            .map(restaurant -> {
                Order newOrder = restaurant.addOrder(orderInput).getBody().get("order"); // Assuming addOrder creates and returns an Order
                return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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
    public void updateOrderStatus(@PathVariable Integer restaurantID, @PathVariable Integer orderID, @RequestBody String status) {

        OrderStatus newStatus = OrderStatus.valueOf(status);

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

    private ResponseEntity<Restaurant> findRestaurantById(Integer restaurantID) {
        return Optional.ofNullable(restaurants.get(restaurantID))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
