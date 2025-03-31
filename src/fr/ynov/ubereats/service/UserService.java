package fr.ynov.ubereats.service;

import fr.ynov.ubereats.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserService {
    private List<User> users = new ArrayList<>();
    private User connectedUser = null;

    public void addUser(User user) {
        users.add(user);
    }

    public Optional<User> findUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }


    public boolean login(String email, String password) {
        Optional<User> user = this.findUser(email);

        if (user.isPresent() && user.get().login(email, password)) {
            System.out.println("Login successful!");
            this.setConnectedUser(user.get());
            return true;
        } else {
            System.out.println("Incorrect email or password.");
            return false;
        }
    }

    private void setConnectedUser(User user) {
        this.connectedUser = user;
    }

    public boolean logout() {
        connectedUser = null;
        return false;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public boolean isLoggedIn() {
        return connectedUser != null;
    }
}