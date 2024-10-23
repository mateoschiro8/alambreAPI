package com.alambre.models;

import java.util.List;

public class RestaurantInput {
    private String name; 
    private Coordinate location;
    private String number;
    private List<String> images;
    private Menu menu; 
    private int numberOfTables; 

    public RestaurantInput(String name, String number, Coordinate location, List<String> images, Menu menu, int numberOfTables) {
        this.name = name;
        this.number = number;
        this.location = location;
        this.images = images;
        this.menu = menu;
        this.numberOfTables = numberOfTables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public List<String> getImages() { return images; }

    public void setImages(List<String> images) { this.images = images; }

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
