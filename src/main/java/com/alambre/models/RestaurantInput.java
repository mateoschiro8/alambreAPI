package com.alambre.models;

import java.util.List;

public class RestaurantInput {
    private String name; 
    private Coordinate location;
    private String number;
    private String logoUrl;
    private List<String> images;
    private List<MenuItem> menu; 
    private int numberOfTables; 
    private String openingTime;
    private String closingTime;

    public RestaurantInput(String name, String number, Coordinate location, String logoUrl, List<String> images, List<MenuItem> menu, int numberOfTables, String openingTime, String closingTime) {
        this.name = name;
        this.location = location;
        this.number = number;
        this.logoUrl = logoUrl;
        this.images = images;
        this.menu = menu;
        this.numberOfTables = numberOfTables;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
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

    public List<String> getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public List<String> getImages() { return images; }

    public void setImages(List<String> images) { this.images = images; }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public String getOpeningTime() { return openingTime; }

    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }

    public String getClosingTime() { return closingTime; }

    public void setClosingTime(String closingTime) { this.closingTime = closingTime; }
}
