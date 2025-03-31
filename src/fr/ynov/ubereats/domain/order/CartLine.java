package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Dish;

public class CartLine {
    private Dish dish;
    private int quantity;

    public CartLine(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void modifyQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return dish.getPrice() * quantity;
    }
}