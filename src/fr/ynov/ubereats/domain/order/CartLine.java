package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Dish;

/**
 * Represents a line in the shopping cart.
 * Each line contains a dish and its quantity.
 * It also provides methods to modify the quantity and calculate the total price.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class CartLine {

    /**
     * The dish in this cart line.
     *The quantity of the dish in this cart line.
     */
    private final Dish dish;
    private int quantity;

    /**
     * Constructor to create a new CartLine with a dish and its quantity.
     *
     * @param dish The dish to be added to the cart line.
     * @param quantity The quantity of the dish in the cart line.
     */
    public CartLine(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    /**
     * Returns the dish in this cart line.
     *
     * @return The dish in this cart line.
     */
    public Dish getDish() {
        return dish;
    }

    /**
     * Returns the quantity of the dish in this cart line.
     *
     * @return The quantity of the dish in this cart line.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Modifies the quantity of the dish in this cart line.
     *
     * @param quantity The new quantity of the dish in this cart line.
     */
    public void modifyQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the total price of this cart line.
     *
     * @return The total price of this cart line.
     */
    public double getTotalPrice() {
        return dish.getPrice() * quantity;
    }
}