package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.user.Customers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private List<Order> orders = new ArrayList<>();

    public void placeOrder(Order order) {
        orders.add(order);
    }

    public Optional<Order> findOrder(String id) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return Optional.of(order);
            }
        }
        return Optional.empty();
    }

    public List<Order> listOrdersByClient(Customers customers) {
        List<Order> clientOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomers().equals(customers)) {
                clientOrders.add(order);
            }
        }
        return clientOrders;
    }

    public List<Order> listOrdersByStatus(OrderStatus status) {
        List<Order> ordersByStatus = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) {
                ordersByStatus.add(order);
            }
        }
        return ordersByStatus;
    }

    public void deleteOrder(String id) {
        orders.removeIf(order -> order.getId().equals(id));
    }

    public boolean updateOrderStatus(String id, OrderStatus newStatus) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order.modifyStatus(newStatus);
            }
        }
        return false;
    }

    public Order getOrderById(long l) {
        return orders.stream().filter(order -> order.getId().equals(l)).findFirst().orElse(null);
    }
}