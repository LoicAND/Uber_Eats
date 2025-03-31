package fr.ynov.ubereats.domain.restaurant;

public class Dish {

    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private boolean available;
    private final Restaurant restaurant;
    private Dish dish;
    private double quantity;

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


    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}