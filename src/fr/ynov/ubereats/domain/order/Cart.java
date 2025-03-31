package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartLine> lines;
    private double totalPrice;
    private Restaurant restaurant;

    public Cart() {
        this.lines = new ArrayList<>();
        this.totalPrice = 0.0;
    }

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

    public void modifyDish(Dish dish, int quantity) {
        for (CartLine line : lines) {
            if (line.getDish().equals(dish)) {
                if (quantity > 0) {
                    line.setQuantity(quantity);
                } else {
                    lines.remove(line);
                }
                calculateTotalPrice();
                return;
            }
        }
        throw new IllegalArgumentException("Dish not found in the cart");
    }

    public void removeDish(Dish dish) {
        lines.removeIf(line -> line.getDish().equals(dish));
        calculateTotalPrice();

        if (lines.isEmpty()) {
            restaurant = null;
        }
    }

    public void clear() {
        lines.clear();
        totalPrice = 0.0;
        restaurant = null;
    }

    public void calculateTotalPrice() {
        totalPrice = lines.stream()
                .mapToDouble(CartLine::getTotalPrice)
                .sum();
    }

    public List<CartLine> getLines() {
        return new ArrayList<>(lines);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}