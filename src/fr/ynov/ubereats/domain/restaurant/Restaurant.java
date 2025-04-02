package fr.ynov.ubereats.domain.restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the Uber Eats application.
 * Each restaurant has a unique identifier, name, address, and a menu of dishes.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Restaurant {
    /**
     * Unique identifier for the restaurant.
     * The name of the restaurant.
     * The physical address of the restaurant.
     * The list of dishes offered by this restaurant, constituting its menu.
     */
    private final String id;
    private String name;
    private String address;
    private final List<Dish> menu;

    /**
     * Constructor to create a new Restaurant with a unique ID, name, and address.
     * Initializes an empty menu.
     *
     * @param id The unique identifier for the restaurant.
     * @param name The name of the restaurant.
     * @param address The physical address of the restaurant.
     */
    public Restaurant(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.menu = new ArrayList<>();
    }


    /**
     * Adds a dish to the restaurant's menu if it's not already present.
     *
     * @param dish The dish to add to the menu.
     */
    public void addDish(Dish dish) {
        if (!menu.contains(dish)) {
            menu.add(dish);
        }
    }

    /**
     * Returns the unique ID of the restaurant.
     *
     * @return The unique ID of the restaurant.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the restaurant.
     *
     * @return The name of the restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the address of the restaurant.
     *
     * @return The address of the restaurant.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns a defensive copy of the restaurant's menu.
     *
     * @return A new ArrayList containing all dishes in the menu.
     */
    public List<Dish> getMenu() {
        return new ArrayList<>(menu);
    }
}