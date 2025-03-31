package fr.ynov.ubereats.domain.order;

import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.user.Customers;
import fr.ynov.ubereats.domain.user.Deliver;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private Customers customers;
    private Restaurant restaurant;
    private Deliver deliveryPerson;
    private List<Dish> lines;
    private double totalPrice;
    private double deliveryFees;
    private OrderStatus status;
    private Date creationDate;
    private Date deliveryDate;
    private String deliveryAddress;

    public Order(String id, Customers customers, Restaurant restaurant) {
        this.id = id;
        this.customers = customers;
        this.restaurant = restaurant;
        this.creationDate = new Date();
        this.status = OrderStatus.CREATED;
    }

    public double calculateTotalPrice() {
        return 0.0;
    }

    public boolean cancel(){
        if (this.status == OrderStatus.CREATED || this.status == OrderStatus.ACCEPTED) {
            this.status = OrderStatus.CANCELLED;
            return true;
        }
        return false;
    }

    public boolean updateStatus(OrderStatus newStatus) {
        if (isTransitionValid(newStatus)) {
            this.status = newStatus;

            if (newStatus == OrderStatus.DELIVERED) {
                this.deliveryDate = new Date();
            }
            return true;
        }
        return false;
    }

    private boolean isTransitionValid(OrderStatus newStatus) {
        switch (this.status) {
            case CREATED:
                return newStatus == OrderStatus.ACCEPTED ||
                        newStatus == OrderStatus.CANCELLED;
            case ACCEPTED:
                return newStatus == OrderStatus.IN_PREPARATION ||
                        newStatus == OrderStatus.CANCELLED;
            case IN_PREPARATION:
                return newStatus == OrderStatus.IN_DELIVERY ||
                        newStatus == OrderStatus.CANCELLED;
            case IN_DELIVERY:
                return newStatus == OrderStatus.DELIVERED ||
                        newStatus == OrderStatus.CANCELLED;
            case DELIVERED:
                return false;
            case CANCELLED:
                return false;
            default:
                return false;
        }
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public List<Dish> getLines() {
        return lines;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDeliveryFees() {
        return deliveryFees;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void addCartLine(Dish line) {
        if (this.lines == null) {
            throw new IllegalStateException("The cart line list is not initialized");
        }
        this.lines.add(line);
        recalculateTotalPrice();
    }

    private void recalculateTotalPrice() {
        if (this.lines == null || this.lines.isEmpty()) {
            this.totalPrice = 0.0;
            return;
        }

        this.totalPrice = this.lines.stream()
                .mapToDouble(line -> line.getDish().getPrice() * line.getQuantity())
                .sum();

        // Add delivery fees if applicable
        if (this.deliveryFees > 0) {
            this.totalPrice += this.deliveryFees;
        }
    }

    public void setDeliveryFees(double fees) {
        this.deliveryFees = fees;
        recalculateTotalPrice();
    }

    public void setDeliveryAddress(String address) {
        this.deliveryAddress = address;
    }

    public int getTotalNumberOfItems() {
        if (this.lines == null) {
            return 0;
        }
        return this.lines.stream()
                .mapToInt(dish -> (int) dish.getQuantity())
                .sum();
    }

    public boolean modifyStatus(OrderStatus newStatus) {
        return this.status == newStatus;
    }
}