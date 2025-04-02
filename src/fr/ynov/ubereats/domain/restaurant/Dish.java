package fr.ynov.ubereats.domain.restaurant;

public class Dish {

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final boolean available;
    private final Restaurant restaurant;

    public Dish(String id, String name, String description, double price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = true;
        this.restaurant = restaurant;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }


    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return available;
    }
}