package fr.ynov.ubereats.domain.payment;

import fr.ynov.ubereats.domain.order.Order;

import java.util.Date;
/**
 * Represents a payment in the Uber Eats application.
 * Each payment is associated with an order and has a unique ID,
 * price, date, payment method, and status.
 * It also provides methods to make the payment and generate a receipt.
 *
 * @author Loïc ANDRIANARIVONY
 */

public class Payment {
    private final String id;
    private final double price;
    private final Date date;
    private final PaymentMethod method;
    private PaymentStatus status;
    private final Order order;
    private double tipAmount = 0.0;

    /**
     * Constructor to create a new Payment with a unique ID,
     * associated order, price, payment method, and status.
     *
     * @param id The unique identifier for the payment.
     * @param order The order associated with this payment.
     * @param price The total price of the payment.
     * @param method The payment method used for this payment.
     */
    public Payment(String id, Order order, double price, PaymentMethod method) {
        this.id = id;
        this.order = order;
        this.price = price;
        this.method = method;
        this.date = new Date();
        this.status = PaymentStatus.ON_HOLD;
    }

    /**
     * The restaurant from which all dishes in this cart are ordered.
     * A cart can only contain dishes from a single restaurant.
     */
    public void makePayment() {
        if (this.status == PaymentStatus.ON_HOLD) {
            this.status = PaymentStatus.ACCEPTED;
        }
    }

    /**
     * Generates a receipt for the payment.
     * The receipt includes the payment ID, date, restaurant name,
     * subtotal, delivery fees, total amount, payment method, and status.
     *
     * @return  The receipt as a formatted string.
     */
    public String receiptOrder(){
        String methodeName = switch(method) {
            case CREDIT_CARD -> "Carte de crédit";
            case PAYPAL -> "PayPal";
            case TICKET -> "Ticket restaurant";
        };

        double subtotal = price - order.getDeliveryFees();

        StringBuilder receipt = new StringBuilder();
        receipt.append("Reçu de paiement\n");
        receipt.append("ID: ").append(id).append("\n");
        receipt.append("Date: ").append(date).append("\n");
        receipt.append("Restaurant: ").append(order.getRestaurant().getName()).append("\n\n");
        receipt.append("Sous-total: ").append(String.format("%.2f€", subtotal)).append("\n");
        receipt.append("Frais de livraison: ").append(String.format("%.2f€", order.getDeliveryFees())).append("\n");
        receipt.append("Montant total: ").append(String.format("%.2f€", price)).append("\n");

        if (tipAmount > 0) {
            receipt.append("Pourboire: ").append(String.format("%.2f€", tipAmount)).append("\n");
            receipt.append("Total avec pourboire: ").append(String.format("%.2f€", price + tipAmount)).append("\n");
        }

        receipt.append("\nMéthode de paiement: ").append(methodeName).append("\n");
        receipt.append("Statut: ").append(status == PaymentStatus.ACCEPTED ? "Payé" : status);

        return receipt.toString();
    }

    /**
     * Returns the unique ID of the payment.
     *
     * @return The unique ID of the payment.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the method of the payment.
     *
     * @return The method of the payment.
     */
    public PaymentMethod getMethod() {
        return method;
    }

    /**
     * Returns the status of the payment.
     *
     * @return The status of the payment.
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * Returns the order for the payment.
     *
     * @return The order for the payment.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Returns the update status of the payment.
     *
     * @param newStatus  The new status to set for the payment.
     */
    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
    }

    public void addTip(double amount){
        if (amount > 0 && this.status == PaymentStatus.ACCEPTED) {
            this.tipAmount = amount;
        }
    }

    public double getTipAmount() {
        return tipAmount;
    }

    public double getTotalAmount() {
        return price + tipAmount;
    }
}
