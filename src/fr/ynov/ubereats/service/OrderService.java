package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.CartLine;
import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.user.Customers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for order management.
 * Handles order creation, modification, status updates, and retrieval.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class OrderService {

    /**
     * Collection of all orders in the system.
     */
    private final List<Order> orders = new ArrayList<>();

    /**
     * Retrieves all orders placed by a specific customer.
     *
     * @param customers The customer whose orders to retrieve
     * @return A list of orders placed by the specified customer
     */
    public List<Order> listOrdersByClient(Customers customers) {
        List<Order> clientOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomers().equals(customers)) {
                clientOrders.add(order);
            }
        }
        return clientOrders;
    }

    /**
     * Updates the status of an order identified by its ID.
     *
     * @param id The ID of the order to update
     * @param newStatus The new status to set for the order
     */
    public void updateOrderStatus(String id, OrderStatus newStatus) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                order.updateStatus(newStatus);
                return;
            }
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to retrieve
     * @return The order if found, null otherwise
     */
    public Order getOrderById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new order in the system.
     *
     * @param id The ID for the new order
     * @param customer The customer placing the order
     * @param restaurant The restaurant from which the order is placed
     * @return The newly created order
     */
    public Order createOrder(String id, Customers customer, fr.ynov.ubereats.domain.restaurant.Restaurant restaurant) {
        Order newOrder = new Order(id, customer, restaurant);
        orders.add(newOrder);
        return newOrder;
    }

    /**
     * Adds a dish with the specified quantity to an existing order.
     * Only works if the order is in the CREATED state.
     *
     * @param orderId The ID of the order to modify
     * @param dish The dish to add to the order
     * @param quantity The quantity of the dish to add
     */
    public void addDishToOrder(String orderId, Dish dish, int quantity) {
        Order order = getOrderById(orderId);

        if (order == null || order.getStatus() != OrderStatus.CREATED) {
            return;
        }

        try {
            CartLine cartLine = new CartLine(dish, quantity);
            order.addCartLine(cartLine);
        } catch (Exception _) {
        }
    }

    /**
     * Cancels an order if it has not been delivered or already canceled.
     *
     * @param orderId The ID of the order to cancel
     * @return true if the order was successfully canceled, false otherwise
     */
    public boolean cancelOrder(String orderId) {
        Optional<Order> orderOpt = findOrderById(orderId);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() != OrderStatus.DELIVERED &&
                    order.getStatus() != OrderStatus.CANCELED) {
                order.updateStatus(OrderStatus.CANCELED);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds an order by its ID.
     *
     * @param id The ID of the order to find
     * @return An Optional containing the order if found, empty otherwise
     */
    public Optional<Order> findOrderById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }
}