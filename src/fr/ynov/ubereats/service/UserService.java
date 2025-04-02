package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.user.Deliver;
import fr.ynov.ubereats.domain.user.User;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;


public class UserService {
    private final List<User> users = new ArrayList<>();
    private User connectedUser = null;

    public void addUser(User user) {
        users.add(user);
    }

    public Optional<User> findUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public List<Deliver> getAllDeliverers() {
        List<Deliver> deliverers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Deliver) {
                deliverers.add((Deliver) user);
            }
        }

        return deliverers;
    }

    public boolean login(String email, String password) {
        Optional<User> user = this.findUser(email);

        if (user.isPresent() && user.get().login(email, password)) {
            this.setConnectedUser(user.get());
            return true;
        }
        return false;
    }

    private void setConnectedUser(User user) {
        this.connectedUser = user;
    }

    public void logout() {
        connectedUser = null;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public boolean isLoggedIn() {
        return connectedUser != null;
    }

    public void logoutUser() {
        this.connectedUser = null;
    }

}
