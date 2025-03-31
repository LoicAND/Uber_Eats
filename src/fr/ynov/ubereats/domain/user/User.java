package fr.ynov.ubereats.domain.user;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;
    protected String address;

    public User(String id, String name, String email, String phone, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        if (!this.email.equalsIgnoreCase(email)) {
            return false;
        }

        return this.password.equals(password);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}