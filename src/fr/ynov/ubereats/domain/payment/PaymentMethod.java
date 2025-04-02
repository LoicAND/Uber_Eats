package fr.ynov.ubereats.domain.payment;

public enum PaymentMethod {
    CREDIT_CARD("Carte bancaire"),
    PAYPAL("Paypal"),
    TICKET("Ticket restaurant");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}
