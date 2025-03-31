package fr.ynov.ubereats.domain.user;

import fr.ynov.ubereats.domain.order.DeliverStatus;
import fr.ynov.ubereats.domain.order.Order;

import java.util.ArrayList;
import java.util.List;

public class Deliver extends User {
    private String vehicule;
    private DeliverStatus status;
    private List<Order> deliver;

    public Deliver(String id, String nom, String email, String telephone, String vehicule) {
        super(id, nom, email, telephone, null, null);
        this.vehicule = vehicule;
        this.status = DeliverStatus.AVAILABLE;
        this.deliver = new ArrayList<>();
    }


    public boolean accepteDeliver(Order order) {
        if (this.status == DeliverStatus.AVAILABLE) {
            this.deliver.add(order);
            this.status = DeliverStatus.IN_DELIVERY;
            return true;
        }
        return false;
    }

    public boolean cancelDeliver(Order order) {
        if (this.status == DeliverStatus.IN_DELIVERY) {
            this.deliver.remove(order);
            this.status = DeliverStatus.AVAILABLE;
            return true;
        }
        return false;
    }

    public boolean enDeliver(Order order) {
        if (this.status == DeliverStatus.IN_DELIVERY && this.deliver.contains(order)) {
            this.deliver.remove(order);
            this.status = DeliverStatus.AVAILABLE;
            return true;
        }
        return false;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public DeliverStatus getStatus() {
        return status;
    }

    public List<Order> getDeliver() {
        return new ArrayList<>(deliver);
    }
}
