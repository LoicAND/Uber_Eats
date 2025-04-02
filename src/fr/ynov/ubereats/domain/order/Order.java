package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Order(String id, Customers customers, Restaurant restaurant) {
        this.id = id;
        this.customers = customers;
        this.restaurant = restaurant;
        this.creationDate = new Date();
        this.status = OrderStatus.CREATED;
        this.lines = new ArrayList<>();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (isTransitionValid(newStatus)) {
            this.status = newStatus;

            if (newStatus == OrderStatus.DELIVERED) {
                this.deliveryDate = new Date();
            }
        }
    }

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

    public OrderStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public Customers getCustomers() {
        return customers;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public List<CartLine> getLines() {
        return lines;
    }

    public void addCartLine(CartLine cartLine) {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot modify an order that has been processed");
        }
        this.lines.add(cartLine);
        recalculateTotalPrice();
    }

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

}