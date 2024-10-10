package com.alambre.controllers;

import com.alambre.models.Restaurant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/alambre")
public class restaurantController {

    private Restaurant asdasd = new Restaurant(2,"a");
    private List<Restaurant> restaurants = new ArrayList<>();

    {
        restaurants.add(asdasd);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @PostMapping("/restaurants")
    public void addRestaurant(@RequestBody Restaurant restaurant) {
        restaurants.add(restaurant);
    }

}
