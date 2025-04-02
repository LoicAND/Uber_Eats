package fr.ynov.ubereats.domain.payment;

/**
 * Enum representing the status of a payment.
 * The status can be one of the following:
 * - ON_HOLD: The payment is on hold.
 * - REFUSED: The payment has been refused.
 * - ACCEPTED: The payment has been accepted.
 * - REFUNDED: The payment has been refunded.
 *
 * @author Loïc ANDRIANARIVONY
 */
public enum PaymentStatus {
    ON_HOLD,
    REFUSED,
    ACCEPTED,
    REFUNDED
}
