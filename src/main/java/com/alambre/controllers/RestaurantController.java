package com.alambre.controllers;

import com.alambre.models.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "https://p11-p11.github.io")
// @CrossOrigin(origins = "http://localhost:4200")
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
    public Restaurant getRestaurant(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID).orElse(null);
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

    @GetMapping("/{restaurantID}/qrs/{tableNumber}")
    public ResponseEntity<byte[]> getQRCode(@PathVariable Integer restaurantID, @PathVariable Integer tableNumber) {
        Optional<Restaurant> restaurantOptional = findRestaurantById(restaurantID);

        if (restaurantOptional.isPresent()) {
            try {
                BufferedImage qrImage = restaurantOptional.get().generateTableQR(tableNumber);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                return ResponseEntity.ok().headers(headers).body(imageBytes);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{restaurantID}/orders")
    public List<Order> getRestaurantOrders(@PathVariable Integer restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getOrders).orElse(null);
    }

    @PostMapping("/{restaurantID}/orders")
    public ResponseEntity<Map<String, Integer>> addOrder(@PathVariable Integer restaurantID, @RequestBody OrderInput orderInput) {
        return findRestaurantById(restaurantID)
                .map(restaurant -> restaurant.addOrder(orderInput))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>()));
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

    private Optional<Restaurant> findRestaurantById(Integer restaurantID) {
        return Optional.ofNullable(restaurants.get(restaurantID));
    }
}
