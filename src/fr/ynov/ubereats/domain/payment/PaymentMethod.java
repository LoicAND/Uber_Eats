package fr.ynov.ubereats.domain.payment;

/**
 * Enum representing the payment methods available in the Uber Eats application.
 * Each payment method has a display name for user-friendly representation.
 *
 * @author Loïc ANDRIANARIVONY
 */
public enum PaymentMethod {
    CREDIT_CARD("Carte bancaire"),
    PAYPAL("Paypal"),
    TICKET("Ticket restaurant");

    /**
     * The user-friendly display name of this payment method.
     */
    private final String displayName;

    /**
     * Returns the user-friendly display name of this payment method.
     *
     * @param displayName  The display name of the payment method.
     */
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}
