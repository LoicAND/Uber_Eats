package fr.ynov.ubereats.domain.user;

import fr.ynov.ubereats.domain.order.Cart;

/**
 * Represents a customer in the Uber Eats application.
 * Customers can create orders, have a shopping cart, and provide delivery information.
 * This class extends the base User class with customer-specific functionality.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Customers extends User {

    /**
     * The shopping cart associated with this customer.
     * Initialized when a customer is created.
     */
    private final Cart cart;

    /**
     * Creates a new customer with the specified personal information.
     * Initializes an empty shopping cart for the customer.
     *
     * @param id Unique identifier for the customer
     * @param name Name of the customer
     * @param email Email address of the customer
     * @param phone Phone number of the customer
     * @param password Customer's account password
     * @param address Delivery address of the customer
     */
    public Customers(String id, String name, String email, String phone, String password, String address) {
        super(id, name, email, phone, password, address);
        this.cart = new Cart();
    }

    /**
     * Returns the shopping cart of the customer.
     *
     * @return The customer's shopping cart
     */
    public Cart getCart() {
        return cart;
    }
}