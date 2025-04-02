package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private final List<Restaurant> restaurants = new ArrayList<>();

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public List<Restaurant> listRestaurants() {
        return new ArrayList<>(restaurants);
    }
}