package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.user.Deliver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeliveryService {
    private UserService userService;
    private OrderService orderService;
    private Map<String, Deliver> orderAssignments = new HashMap<>();

    public DeliveryService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    public boolean assignDelivererToOrder(Deliver deliverer, String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getStatus() != OrderStatus.IN_PREPARATION) {
            return false;
        }

        boolean accepted = deliverer.accepteDelivery(order);
        if (accepted) {
            orderAssignments.put(orderId, deliverer);
        }
        return accepted;
    }
    public List<Deliver> getAvailableDeliverers() {
        List<Deliver> allDeliverers = userService.getAllDeliverers();
        return allDeliverers.stream()
                .filter(deliver ->!orderAssignments.containsValue(deliver))
                .collect(Collectors.toList());
    }

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

    public Deliver getDelivererForOrder(String orderId) {
        return orderAssignments.get(orderId);
    }

    public void completeDelivery(String orderId) {
        orderAssignments.remove(orderId);
    }

    public Deliver autoAssignDeliverer(String orderId) {
        List<Deliver> availableDeliverers = getAvailableDeliverers();
        if (availableDeliverers.isEmpty()) {
            return null;
        }

        Deliver selectedDeliverer = availableDeliverers.get(0);
        boolean assigned = assignDelivererToOrder(orderId, selectedDeliverer);

        return assigned ? selectedDeliverer : null;
    }
}
