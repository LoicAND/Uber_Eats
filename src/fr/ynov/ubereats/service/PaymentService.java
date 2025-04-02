package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.payment.PaymentMethod;
import fr.ynov.ubereats.domain.payment.Payment;
import fr.ynov.ubereats.domain.payment.PaymentStatus;

import java.util.ArrayList;
import java.util.List;


public class PaymentService {
    private final List<Payment> payments = new ArrayList<>();

    public void savePayment(Payment payment) {
        payments.add(payment);
    }

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