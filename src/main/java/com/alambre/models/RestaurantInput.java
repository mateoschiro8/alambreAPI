package com.alambre.models;

import java.util.List;

public class RestaurantInput {
    private String name; 
    private Coordinate location; 
    private Menu menu; 
    private int numberOfTables; 

    public RestaurantInput(String name, Coordinate location, Menu menu, int numberOfTables) {
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.numberOfTables = numberOfTables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }
}
