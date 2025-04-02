package fr.ynov.ubereats.domain.order;
/**
 * Enum representing the status of an order.
 * The status can be one of the following:
 * - CREATED: The order has been created.
 * - IN_PREPARATION: The order is being prepared.
 * - IN_DELIVERY: The order is out for delivery.
 * - DELIVERED: The order has been delivered.
 * - ACCEPTED: The order has been accepted by the restaurant.
 * - CANCELED: The order has been canceled.
 *
 * @author Loïc ANDRIANARIVONY
 */

public enum OrderStatus {
    CREATED,
    IN_PREPARATION,
    IN_DELIVERY,
    DELIVERED,
    ACCEPTED,
    CANCELED
}
