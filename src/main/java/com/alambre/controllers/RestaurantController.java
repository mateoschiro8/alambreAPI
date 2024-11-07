package com.alambre.controllers;

import com.alambre.models.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.alambre.models.OrderStatus;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restaurants")
public class RestaurantController {

    private ConcurrentHashMap<UUID, Restaurant> restaurants = new ConcurrentHashMap<>();

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
        Restaurant restaurant = new Restaurant(restaurantInput);       
        restaurants.put(restaurant.getID(), restaurant);
    }

    @GetMapping("/{restaurantID}")
    public Restaurant getRestaurant(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).orElse(null);
    }

    // Aca habria que pegarle desde el qr, o ver de armar otro endpoint
    @GetMapping("/{restaurantID}/menu")
    public List<MenuItem> getMenu(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getMenu).orElse(null);
    }

    @GetMapping("/{restaurantID}/orders")
    public List<Order> getRestaurantOrders(@PathVariable UUID restaurantID) {
        return findRestaurantById(restaurantID).map(Restaurant::getOrders).orElse(null);
    }

    @PostMapping("/{restaurantID}/orders")
    public boolean addOrder(@PathVariable UUID restaurantID, @RequestBody OrderInput orderInput) {
        return findRestaurantById(restaurantID)
                .map(restaurant -> restaurant.addOrder(orderInput))
                .orElse(false);
    }
    
    @GetMapping("/{restaurantID}/orders/{orderID}")
    public Order getOrder(@PathVariable UUID restaurantID, @PathVariable UUID orderID) {
        return findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst())
                .orElse(null);
    }

    @PatchMapping("/{restaurantID}/orders/{orderID}")
    public void updateOrderStatus(@PathVariable UUID restaurantID, @PathVariable UUID orderID, @RequestBody OrderStatus newStatus) {
        
        Optional<Restaurant> rest = findRestaurantById(restaurantID);
        Optional<Order> orderOptional = findRestaurantById(restaurantID)
                .flatMap(restaurant -> restaurant.getOrders().stream()
                        .filter(order -> order.getId().equals(orderID))
                        .findFirst());

        if (orderOptional.isPresent()) {
            orderOptional.get().updateStatus(newStatus);
            if(newStatus == OrderStatus.DELIVERED)
                rest.get().emptyTable(orderID);     
        } else {
            throw new IllegalStateException("Order not found");
        }
    }

    private Optional<Restaurant> findRestaurantById(UUID restaurantID) {
        return Optional.ofNullable(restaurants.get(restaurantID));
    }

    // TODO endpoint desocupar una mesa



    /*

    @PostMapping("/{restaurantID}/customers/{customerID}/orders")
    public Order createOnlineCustomerOrder(@PathVariable UUID restaurantID, @PathVariable UUID customerID, @RequestBody List<OrderItem> items) {
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        Optional<OnlineCustomer> customer = findOnlineCustomerById(restaurantID, customerID);
        if (restaurant.isPresent() && customer.isPresent()) {
            // chequear que currentOrder sea null o este en estado delivered
            Order newOrder = new Order(Optional.of(customerID), Optional.empty(), items);
            restaurant.get().addOrder(newOrder);
            customer.get().setCurrentOrder(newOrder);
            return newOrder;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
    }

    @PostMapping("/{restaurantID}/customers")
    public OnlineCustomer addCustomer(@PathVariable UUID restaurantID, @RequestBody String email) {
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        if (restaurant.isPresent()) {
            OnlineCustomer newCustomer = new OnlineCustomer(email);
            restaurant.get().addCustomer(newCustomer);
            return newCustomer;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
    }

    @GetMapping("/{restaurantID}/tables")
    public List<Table> getTables(@PathVariable UUID restaurantID) {
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        return restaurant.map(Restaurant::getTables).orElse(Collections.emptyList());
    }

    @PutMapping("/{restaurantID}/tables/{tableID}")
    public void occupyTable(@PathVariable UUID restaurantID, @PathVariable UUID tableID) {
        Optional<Table> table = findTableById(restaurantID, tableID);
        if (table.isPresent()) {
            Table occupiedTable = table.get();
            // TODO meter este chequeo dentro del isOccupied
            if (occupiedTable.isOccupied()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Table is already occupied");
            }
            occupiedTable.setOccupied(true);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");
        }
    }



    @PostMapping("/{restaurantID}/tables/{tableID}/orders")
    public Order createTableOrder(@PathVariable UUID restaurantID, @PathVariable UUID tableID, @RequestBody List<OrderItem> items) {
        // TODO aca validar que este a menos de 50mts y que currentOrder sea null o este en estado delivered
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        Optional<Table> table = findTableById(restaurantID, tableID);
        if (restaurant.isPresent() && table.isPresent()) {
            Order newOrder = new Order(Optional.empty(), Optional.of(tableID), items);
            restaurant.get().addOrder(newOrder);
            table.get().setCurrentOrder(newOrder);
            return newOrder;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
    }

     private Optional<Table> findTableById(UUID restaurantID, UUID tableID) {
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        if (restaurant.isPresent()) {
            return restaurant.get().getTables().stream()
                    .filter(table -> table.getTableID().equals(tableID))
                    .findFirst();
        }
        return Optional.empty();
    }
    private Optional<OnlineCustomer> findOnlineCustomerById(UUID restaurantID, UUID customerID) {
        Optional<Restaurant> restaurant = findRestaurantById(restaurantID);
        if (restaurant.isPresent()) {
            return restaurant.get().getCustomers().stream()
                    .filter(customer -> customer.getCustomerID().equals(customerID))
                    .findFirst();
        }
        return Optional.empty();
    }
     */
}
