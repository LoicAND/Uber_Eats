package fr.ynov.ubereats.domain.user;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.order.Cart;
import fr.ynov.ubereats.domain.restaurant.Dish;
import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Customers extends User {
    private Cart cart;
    private List<Order> orderHistory;

    public Customers(String id, String name, String email, String phone, String password, String address) {
        super(id, name, email, phone, password, address);
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>();
    }

    public List<Restaurant> browseRestaurants() {
        return new ArrayList<>();
    }

    public void addToCart(Restaurant restaurant, Dish dish, int quantity) {
        cart.addDish(dish, quantity);
    }

    public Order placeOrder() {
        String orderId = "ORDER-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
        Restaurant restaurant = cart.getRestaurant();
        Order newOrder = new Order(orderId, this, restaurant);
        orderHistory.add(newOrder);
        cart.clear();
        return newOrder;
    }

    public void rateOrder(Order order, int rating) {}

    public Cart getCart() {
        return cart;
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }
}