package fr.ynov.ubereats.domain.user;

/**
 * Abstract base class representing a user in the Uber Eats application.
 * This class defines common properties and behaviors for all types of users
 * in the system (customers, delivery personnel, restaurant administrators, etc.).
 *
 * Each user has identification information, contact details, and authentication credentials.
 * Specific user types extend this class with their own specialized functionality.
 *
 * @author Loïc ANDRIANARIVONY
 */
public abstract class User {

    /**
     * Unique identifier for the user.
     * The name of the user.
     * Email address of the user, used for login and communication.
     * Phone number of the user, used for contact purposes.
     * Password for user authentication, stored securely.
     * Physical address of the user, used for delivery purposes.
     */
    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;
    protected String address;


    /**
     * Creates a new user with the specified personal information.
     *
     * @param id Unique identifier for the user
     * @param name Name of the user
     * @param email Email address of the user
     * @param phone Phone number of the user
     * @param password User's account password (may be null for some user types)
     * @param address Physical address of the user (may be null for some user types)
     */
    public User(String id, String name, String email, String phone, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param email The email address to check
     * @param password The password to verify
     * @return true if the credentials match, false otherwise
     */
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        if (!this.email.equalsIgnoreCase(email)) {
            return false;
        }

        return this.password.equals(password);
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return The user's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the user.
     *
     * @return The user's name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the email address of the user.
     *
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }


}