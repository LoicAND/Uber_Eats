package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a shopping cart for food ordering.
 * The cart can contain multiple dishes from a single restaurant.
 * It keeps track of all added items and maintains the total price.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Cart {

    /**
     * The list of cart lines, each representing a dish and its quantity.
     * The total price of all items in the cart.
     * The restaurant from which all dishes in this cart are ordered.
     * A cart can only contain dishes from a single restaurant.
     */

    private final List<CartLine> lines;
    private double totalPrice;
    private Restaurant restaurant;

    public Cart() {
        this.lines = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    /**
     * Returns the list of cart lines.
     *
     * @return the list of cart lines
     */
    public void addDish(Dish dish, int quantity) {
        if (restaurant == null || restaurant.equals(dish.getRestaurant())) {
            for (CartLine line : lines) {
                if (line.getDish().equals(dish)) {
                    line.modifyQuantity(line.getQuantity() + quantity);
                    calculateTotalPrice();
                    return;
                }
            }

            CartLine newLine = new CartLine(dish, quantity);
            lines.add(newLine);

            if (restaurant == null) {
                restaurant = dish.getRestaurant();
            }

            calculateTotalPrice();
        } else {
            throw new IllegalArgumentException("Cannot add a dish from another restaurant");
        }
    }

    /**
     * Returns the list of cart lines.
     *
     * @return the list of cart lines
     */
    public void clear() {
        lines.clear();
        totalPrice = 0.0;
        restaurant = null;
    }

    /**
     * Returns the total price of all items in the cart.
     *
     * @return the total price
     */
    public void calculateTotalPrice() {
        totalPrice = lines.stream()
                .mapToDouble(CartLine::getTotalPrice)
                .sum();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

}