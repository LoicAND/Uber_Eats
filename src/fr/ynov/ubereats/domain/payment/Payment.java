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

    public String receiptOrder(){
        String methodeName = switch(methode) {
            case CREDIT_CARD -> "Carte de crédit";
            case PAYPAL -> "PayPal";
            case TICKET -> "Ticket restaurant";
        };

        double subtotal = price - order.getDeliveryFees();

        return "Reçu de paiement\n" +
                "ID: " + id + "\n" +
                "Date: " + date + "\n" +
                "Restaurant: " + order.getRestaurant().getName() + "\n\n" +
                "Sous-total: " + String.format("%.2f€", subtotal) + "\n" +
                "Frais de livraison: " + String.format("%.2f€", order.getDeliveryFees()) + "\n" +
                "Montant total: " + String.format("%.2f€", price) + "\n\n" +
                "Méthode de paiement: " + methodeName + "\n" +
                "Statut: " + (status == PaymentStatus.ACCEPTED ? "Payé" : status);
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


