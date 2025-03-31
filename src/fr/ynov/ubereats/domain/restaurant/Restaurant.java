package fr.ynov.ubereats.domain.restaurant;

import fr.ynov.ubereats.domain.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private List<Dish> menu;
    private boolean isOpen;

    public Restaurant(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.menu = new ArrayList<>();
        this.isOpen = false;
    }

    public void addDish(Dish dish) {
        if (!menu.contains(dish)) {
            menu.add(dish);
        }
    }

    public void removeDish(Dish dish) {
        menu.remove(dish);
    }

    public void updateInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public boolean acceptOrder(Order order) {
        return true;
    }

    public boolean rejectOrder(Order order) {
        return true;
    }

    public void reportProblem(String description){}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Dish> getMenu() {
        return new ArrayList<>(menu);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}