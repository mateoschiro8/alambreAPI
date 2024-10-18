package com.alambre.models;

import java.util.List;

public class Menu {

    private List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        this.items = items;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
