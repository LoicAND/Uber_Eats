package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.user.Deliver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for managing delivery operations.
 * Handles assignment of deliverers to orders, tracking of deliveries,
 * and management of deliverer availability.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class DeliveryService {

    /**
     * Service for accessing user information, particularly deliverers.
     * Service for accessing and manipulating order information.
     * Mapping between order IDs and their assigned deliverers.
     * If an order ID is present as a key, it has been assigned to a deliverer.
     */
    private final UserService userService;
    private final OrderService orderService;
    private final Map<String, Deliver> orderAssignments = new HashMap<>();

    /**
     * Constructs a new DeliveryService with the required dependencies.
     *
     * @param orderService The service for accessing order information
     * @param userService The service for accessing user information
     */
    public DeliveryService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Retrieves all deliverers who are currently available to accept orders.
     * A deliverer is considered available if they are not currently assigned to any order.
     *
     * @return A list of available deliverers
     */
    public List<Deliver> getAvailableDeliverers() {
        List<Deliver> allDeliverers = userService.getAllDeliverers();
        return allDeliverers.stream()
                .filter(deliver ->!orderAssignments.containsValue(deliver))
                .collect(Collectors.toList());
    }


    /**
     * Assigns a specific deliverer to an order.
     * The order must be in the IN_PREPARATION status and the deliverer must be available.
     *
     * @param orderId The ID of the order to assign
     * @param deliverer The deliverer to assign to the order
     * @return true if the assignment is successful, false otherwise
     */
    public boolean assignDelivererToOrder(String orderId, Deliver deliverer) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getStatus() != OrderStatus.IN_PREPARATION) {
            return false;
        }

        if (orderAssignments.containsValue(deliverer)) {
            return false;
        }

        orderAssignments.put(orderId, deliverer);
        return true;
    }

    /**
     * Retrieves the deliverer assigned to a specific order.
     *
     * @param orderId The ID of the order
     * @return The assigned deliverer, or null if no deliverer is assigned
     */
    public Deliver getDelivererForOrder(String orderId) {
        return orderAssignments.get(orderId);
    }

    /**
     * Marks an order delivery as complete and releases the assigned deliverer.
     * This makes the deliverer available for other orders.
     *
     * @param orderId The ID of the completed order
     */
    public void completeDelivery(String orderId) {
        orderAssignments.remove(orderId);
    }

    /**
     * Automatically assigns the first available deliverer to an order.
     * This is useful for automatic assignment when a customer doesn't have a preference.
     *
     * @param orderId The ID of the order that needs a deliverer
     * @return The assigned deliverer, or null if no deliverer could be assigned
     */
    public Deliver autoAssignDeliverer(String orderId) {
        List<Deliver> availableDeliverers = getAvailableDeliverers();
        if (availableDeliverers.isEmpty()) {
            return null;
        }

        Deliver selectedDeliverer = availableDeliverers.getFirst();
        boolean assigned = assignDelivererToOrder(orderId, selectedDeliverer);

        return assigned ? selectedDeliverer : null;
    }
}
