package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.payment.PaymentMethod;
import fr.ynov.ubereats.domain.payment.Payment;
import fr.ynov.ubereats.domain.payment.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for payment processing.
 * Handles payment creation, processing, and status management.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class PaymentService {

    /**
     * Collection of all payments processed by the system.
     */
    private final List<Payment> payments = new ArrayList<>();

    /**
     * Stores a payment in the system.
     *
     * @param payment The payment to save
     */
    public void savePayment(Payment payment) {
        payments.add(payment);
    }

    /**
     * Creates and processes a new payment for an order.
     *
     * @param order The order to be paid for
     * @param method The payment method to use
     * @return The created payment with its final status
     */
    public Payment createPayment(Order order, PaymentMethod method) {
        String paymentId = "PAY-" + System.currentTimeMillis();
        Payment payment = new Payment(paymentId, order, order.getTotalPrice(), method);

        boolean success = processPaymentWithMethod(method);
        if (success) {
            payment.makePayment();
        } else {
            payment.updateStatus(PaymentStatus.REFUSED);
        }

        savePayment(payment);
        return payment;
    }

    /**
     * Processes a payment using the specified payment method.
     * Simulates a payment processing delay for realism.
     *
     * @param method The payment method to process with
     * @return true if payment succeeded, false otherwise
     */
    private boolean processPaymentWithMethod(PaymentMethod method) {

        try {

            Thread.sleep(1000);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

}