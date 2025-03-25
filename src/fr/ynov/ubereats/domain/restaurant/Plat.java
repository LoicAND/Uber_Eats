package fr.ynov.ubereats.domain.restaurant;

public class Plat {

    private String id;
    private String nom;
    private String description;
    private String img;
    private double prix;
    private boolean disponible;
    private Restaurant restaurant;

    public Plat(String id, String nom, String description, double prix, Restaurant restaurant) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.disponible = true;
        this.restaurant = restaurant;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public double getPrix() {
        return prix;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}

