package fr.ynov.ubereats.domain.user;

import fr.ynov.ubereats.domain.order.Cart;

public class Customers extends User {

    public Customers(String id, String name, String email, String phone, String password, String address) {
        super(id, name, email, phone, password, address);
        Cart cart = new Cart();
    }

}