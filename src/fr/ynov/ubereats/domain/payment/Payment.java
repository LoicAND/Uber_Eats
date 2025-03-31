package fr.ynov.ubereats.domain.payment;

import fr.ynov.ubereats.domain.order.Order;

import java.util.Date;

public class Payment {
    private String id;
    private double price;
    private Date date;
    private PaymentMethod methode;
    private PaymentStatus statut;
    private Order order;

    public Payment(String id, Order order, double price, PaymentMethod methode) {
        this.id = id;
        this.order = order;
        this.price = price;
        this.methode = methode;
        this.date = new Date();
        this.statut = PaymentStatus.ON_HOLD;
    }

    public boolean makePayment() {
        if (this.statut == PaymentStatus.ON_HOLD) {
            this.statut = PaymentStatus.ACCEPTED;
            return true;
        }
        return false;
    }

    public boolean cancelPayment() {
        if (this.statut == PaymentStatus.ACCEPTED || this.statut == PaymentStatus.ON_HOLD) {
            this.statut = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }

    public String genereRecu(){
        return "Reçu de paiement\n" +
                "ID: " + id + "\n" +
                "Montant: " + price + "\n" +
                "Date: " + date + "\n" +
                "Méthode: " + methode + "\n" +
                "Statut: " + statut;
    }

    public String getId() {
        return id;
    }


    public PaymentMethod getMethod() {
        return methode;
    }

    public PaymentStatus getStatus() {
        return statut;
    }

    public Order getOrder() {
        return order;
    }
}


