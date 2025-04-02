package fr.ynov.ubereats.domain.restaurant;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final String id;
    private String name;
    private String address;
    private final List<Dish> menu;

    public Restaurant(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.menu = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        if (!menu.contains(dish)) {
            menu.add(dish);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Dish> getMenu() {
        return new ArrayList<>(menu);
    }

}