package fr.ynov.ubereats.domain.user;

/**
 * Represents a delivery person in the Uber Eats application.
 * Delivery personnel are responsible for transporting orders from restaurants to customers.
 * This class extends the base User class with delivery-specific functionality.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class Deliver extends User {

    /**
     * The type of vehicle used by the delivery person.
     * This cannot be changed after the delivery person is created.
     */
    private final String vehicle;

    /**
     * Creates a new delivery person with the specified personal information.
     * Delivery personnel don't require a password or address as they use a different authentication method.
     *
     * @param id Unique identifier for the delivery person
     * @param name Name of the delivery person
     * @param email Email address of the delivery person
     * @param phone Phone number of the delivery person
     * @param vehicle Type of vehicle used for deliveries (e.g., "bicycle", "car", "scooter")
     */
    public Deliver(String id, String name, String email, String phone, String vehicle) {
        super(id, name, email, phone, null, null);
        this.vehicle = vehicle;
    }

    /**
     * Returns the type of vehicle used by this delivery person.
     *
     * @return The type of vehicle used for deliveries
     */
    public String getVehicleType() {
        return vehicle;
    }
}