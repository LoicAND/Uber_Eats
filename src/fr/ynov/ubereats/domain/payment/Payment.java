package fr.ynov.ubereats.domain.payment;

import fr.ynov.ubereats.domain.order.Order;

import java.util.Date;

public class Payment {
    private final String id;
    private final double price;
    private final Date date;
    private final PaymentMethod methode;
    private PaymentStatus status;
    private final Order order;

    public Payment(String id, Order order, double price, PaymentMethod methode) {
        this.id = id;
        this.order = order;
        this.price = price;
        this.methode = methode;
        this.date = new Date();
        this.status = PaymentStatus.ON_HOLD;
    }

    public void makePayment() {
        if (this.status == PaymentStatus.ON_HOLD) {
            this.status = PaymentStatus.ACCEPTED;
        }
    }

    public String genereRecu(){
        return "Reçu de paiement\n" +
                "ID: " + id + "\n" +
                "Montant: " + price + "\n" +
                "Date: " + date + "\n" +
                "Méthode: " + methode + "\n" +
                "Statut: " + status;
    }

    public String getId() {
        return id;
    }


    public PaymentMethod getMethod() {
        return methode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }


    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
    }
}


