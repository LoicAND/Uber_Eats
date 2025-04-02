package fr.ynov.ubereats.domain.order;

/**
 * Enum representing the delivery status of an order.
 * The status can be one of the following:
 * - AVAILABLE: The order is available for delivery.
 * - IN_DELIVERY: The order is currently being delivered.
 * - UNAVAILABLE: The order is unavailable for delivery.
 *
 * @author Loïc ANDRIANARIVONY
 */

public enum DeliverStatus {
    AVAILABLE,
    IN_DELIVERY,
    UNAVAILABLE,
}
