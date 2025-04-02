package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for restaurant management.
 * Handles restaurant registration and listing operations.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class RestaurantService {

    /**
     * Collection of all restaurants registered in the system.
     */
    private final List<Restaurant> restaurants = new ArrayList<>();

    /**
     * Adds a new restaurant to the system.
     *
     * @param restaurant The restaurant to add
     */
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    /**
     * Lists all available restaurants in the system.
     *
     * @return A list containing all registered restaurants
     */
    public List<Restaurant> listRestaurants() {
        return new ArrayList<>(restaurants);
    }
}