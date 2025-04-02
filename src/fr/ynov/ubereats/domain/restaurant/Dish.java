package fr.ynov.ubereats.domain.restaurant;

/**
 * Class representing a dish in the Uber Eats application.
 * Each dish has an ID, name, description, price, availability status, and the restaurant it belongs to.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Dish {

    /**
     * Unique identifier for the dish.
     * name: The name of the dish.
     * description: A brief description of the dish.
     * price: The price of the dish.
     * available: Indicates if the dish is available for order.
     * restaurant: The restaurant that offers this dish.
     */
    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final boolean available;
    private final Restaurant restaurant;

    /**
     * Constructor to create a new Dish with a unique ID, name, description, price, and restaurant.
     *
     * @param id The unique identifier for the dish.
     * @param name The name of the dish.
     * @param description A brief description of the dish.
     * @param price The price of the dish.
     * @param restaurant The restaurant that offers this dish.
     */
    public Dish(String id, String name, String description, double price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = true;
        this.restaurant = restaurant;
    }

    /**
     * Unique identifier for the dish.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the dish.
     *
     * @return The name of the dish.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the dish.
     *
     * @return The price of the dish.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the restaurant that offers this dish.
     *
     * @return The restaurant that offers this dish.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Returns a brief description of the dish.
     *
     * @return A brief description of the dish.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the availability status of the dish.
     *
     * @return true if the dish is available, false otherwise.
     */
    public boolean isAvailable() {
        return available;
    }
}