package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.user.Deliver;
import fr.ynov.ubereats.domain.user.User;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

/**
 * Service responsible for user management.
 * Handles user authentication, registration, and user-related operations.
 *
 * @author Loïc ANDRIANARIVONY
 */
public class UserService {

    /**
     * Collection of all users registered in the system.
     * Currently authenticated user, or null if no user is logged in.
     */
    private final List<User> users = new ArrayList<>();
    private User connectedUser = null;

    /**
     * Adds a new user to the system.
     *
     * @param user The user to add
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    public Optional<User> findUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Retrieves all delivery personnel registered in the system.
     *
     * @return A list of all available deliverers
     */
    public List<Deliver> getAllDeliverers() {
        List<Deliver> deliverers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Deliver) {
                deliverers.add((Deliver) user);
            }
        }

        return deliverers;
    }

    /**
     * Authenticates a user with the provided credentials.
     * If successful, sets the user as the currently connected user.
     *
     * @param email The user's email address
     * @param password The user's password
     * @return true if authentication succeeded, false otherwise
     */
    public boolean login(String email, String password) {
        Optional<User> user = this.findUser(email);

        if (user.isPresent() && user.get().login(email, password)) {
            this.setConnectedUser(user.get());
            return true;
        }
        return false;
    }

    /**
     * Sets the currently connected user.
     * This is an internal method used after successful authentication.
     *
     * @param user The user to set as connected
     */
    private void setConnectedUser(User user) {
        this.connectedUser = user;
    }

    /**
     * Logs out the currently connected user.
     */
    public void logout() {
        connectedUser = null;
    }

    /**
     * Gets the currently connected user.
     *
     * @return The currently connected user, or null if no user is logged in
     */
    public User getConnectedUser() {
        return connectedUser;
    }


    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return connectedUser != null;
    }
}
