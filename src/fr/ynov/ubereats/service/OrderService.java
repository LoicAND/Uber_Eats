package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.CartLine;
import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.OrderStatus;
import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.user.Customers;

import java.util.ArrayList;
import java.util.List;


public class OrderService {
    private List<Order> orders = new ArrayList<>();


    public List<Order> listOrdersByClient(Customers customers) {
        List<Order> clientOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomers().equals(customers)) {
                clientOrders.add(order);
            }
        }
        return clientOrders;
    }



    public boolean updateOrderStatus(String id, OrderStatus newStatus) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order.updateStatus(newStatus);
            }
        }
        return false;
    }

    public Order getOrderById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Order createOrder(String id, Customers customer, fr.ynov.ubereats.domain.restaurant.Restaurant restaurant) {
        Order newOrder = new Order(id, customer, restaurant);
        orders.add(newOrder);
        return newOrder;
    }

    public boolean addDishToOrder(String orderId, Dish dish, int quantity) {
        Order order = getOrderById(orderId);

        if (order == null || order.getStatus() != OrderStatus.CREATED) {
            return false;
        }

        try {
            CartLine cartLine = new CartLine(dish, quantity);
            order.addCartLine(cartLine);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}