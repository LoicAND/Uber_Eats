package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an order in the Uber Eats application.
 * Each order is associated with a customer, a restaurant,
 * and contains a list of cart lines (dishes).
 * It also tracks the order status, total price, and delivery fees.
 *
 * @author Loïc ANDRIANARIVONY
 */

public class Order {
    private final String id;
    private final Customers customers;
    private final Restaurant restaurant;
    private final List<CartLine> lines;
    private double totalPrice;
    private double deliveryFees;
    private OrderStatus status;
    private final Date creationDate;
    private Date deliveryDate;

    /**
     * Constructor to create a new Order with a unique ID,
     * associated customer, and restaurant.
     *
     * @param id The unique identifier for the order.
     * @param customers The customer placing the order.
     * @param restaurant The restaurant from which the order is placed.
     */
    public Order(String id, Customers customers, Restaurant restaurant) {
        this.id = id;
        this.customers = customers;
        this.restaurant = restaurant;
        this.creationDate = new Date();
        this.status = OrderStatus.CREATED;
        this.lines = new ArrayList<>();
        this.deliveryFees = calculateDeliveryFees(customers, restaurant);
    }

    /**
     * Calculates the delivery fees based on the customer and restaurant.
     * This is a placeholder implementation and should be replaced with
     * actual logic to calculate delivery fees based on distance, etc.
     *
     * @param customers The customer placing the order.
     * @param restaurant The restaurant from which the order is placed.
     * @return The calculated delivery fees.
     */
    private double calculateDeliveryFees(Customers customers, Restaurant restaurant) {
        double baseRate = 2.99;
        double distanceRate = 0.5;
        double estimatedDistance = Math.random() * 10;
        return Math.round((baseRate + (distanceRate * estimatedDistance)) * 100.0) / 100.0;
    }
    /**
     * Updates the status of the order.
     * The status can only be changed if the transition is valid.
     *
     * @param newStatus The new status to set for the order.
     */
    public void updateStatus(OrderStatus newStatus) {
        if (isTransitionValid(newStatus)) {
            this.status = newStatus;

            if (newStatus == OrderStatus.DELIVERED) {
                this.deliveryDate = new Date();
            }
        }
    }

    /**
     * Checks if the transition from the current status to the new status is valid.
     *
     * @param newStatus The new status to check.
     * @return true if the transition is valid, false otherwise.
     */
    private boolean isTransitionValid(OrderStatus newStatus) {
        return switch (this.status) {
            case CREATED -> newStatus == OrderStatus.ACCEPTED ||
                    newStatus == OrderStatus.CANCELED;
            case ACCEPTED -> newStatus == OrderStatus.IN_PREPARATION ||
                    newStatus == OrderStatus.CANCELED;
            case IN_PREPARATION -> newStatus == OrderStatus.IN_DELIVERY ||
                    newStatus == OrderStatus.CANCELED;
            case IN_DELIVERY -> newStatus == OrderStatus.DELIVERED ||
                    newStatus == OrderStatus.CANCELED;
            case DELIVERED -> false;
            case CANCELED -> false;
        };
    }
    /**
     * Adds a cart line (dish) to the order.
     * The order can only be modified if it is in the CREATED status.
     *
     * @param cartLine The cart line to add to the order.
     */
    public void addCartLine(CartLine cartLine) {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot modify an order that has been processed");
        }
        this.lines.add(cartLine);
        recalculateTotalPrice();
    }

    /**
     * Removes a cart line (dish) from the order.
     * The order can only be modified if it is in the CREATED status.
     */
    private void recalculateTotalPrice() {
        if (this.lines.isEmpty()) {
            this.totalPrice = 0.0;
            return;
        }

        this.totalPrice = this.lines.stream()
                .mapToDouble(CartLine::getTotalPrice)
                .sum();

        if (this.deliveryFees > 0) {
            this.totalPrice += this.deliveryFees;
        }
    }
    /**
     * Clears the order by removing all cart lines and resetting the total price.
     * The order can only be modified if it is in the CREATED status.
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Returns the unique identifier of the order.
     *
     * @return The unique identifier of the order.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the customer associated with the order.
     *
     * @return The customer associated with the order.
     */
    public Customers getCustomers() {
        return customers;
    }

    /**
     * Returns the restaurant associated with the order.
     *
     * @return The restaurant associated with the order.
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Returns the date when the order was created.
     *
     * @return The date when the order was created.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the date when the order was delivered.
     *
     * @return The date when the order was delivered.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns the list of cart lines (dishes) in the order.
     *
     * @return The list of cart lines in the order.
     */
    public List<CartLine> getLines() {
        return lines;
    }

    /**
     * Returns the delivery fees for the order.
     *
     * @return The delivery fees for the order.
     */
    public double getDeliveryFees() {
        return deliveryFees;
    }
}