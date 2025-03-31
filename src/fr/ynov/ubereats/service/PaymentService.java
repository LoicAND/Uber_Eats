package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.order.Order;
import fr.ynov.ubereats.domain.payment.PaymentMethod;
import fr.ynov.ubereats.domain.payment.Payment;
import fr.ynov.ubereats.domain.payment.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentService {
    private List<Payment> payments = new ArrayList<>();

    public void savePayment(Payment payment) {
        payments.add(payment);
    }

    public Optional<Payment> findPayment(String id) {
        for (Payment payment : payments) {
            if (payment.getId().equals(id)) {
                return Optional.of(payment);
            }
        }
        return Optional.empty();
    }

    public Optional<Payment> findPaymentForOrder(Order order) {
        for (Payment payment : payments) {
            if (payment.getOrder().equals(order)) {
                return Optional.of(payment);
            }
        }
        return Optional.empty();
    }

    public List<Payment> listPaymentsByMethod(PaymentMethod method) {
        List<Payment> paymentsByMethod = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getMethod() == method) {
                paymentsByMethod.add(payment);
            }
        }
        return paymentsByMethod;
    }

    public List<Payment> listPaymentsByStatus(PaymentStatus status) {
        List<Payment> paymentsByStatus = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getStatus() == status) {
                paymentsByStatus.add(payment);
            }
        }
        return paymentsByStatus;
    }

    public void deletePayment(String id) {
        payments.removeIf(payment -> payment.getId().equals(id));
    }
}