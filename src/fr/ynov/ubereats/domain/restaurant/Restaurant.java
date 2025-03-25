package fr.ynov.ubereats.domain.restaurant;

import fr.ynov.ubereats.domain.commande.Commande;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String id;
    private String nom;
    private String adresse;
    private List<Plat> menu;
    private boolean ouvert;

    public Restaurant(String id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.menu = new ArrayList<>();
        this.ouvert = false;
    }

    public void ajouterPlat(Plat plat) {
        if (!menu.contains(plat)) {
            menu.add(plat);
        }
    }

    public void supprimerPlat(Plat plat) {
        menu.remove(plat);
    }

    public void modifierInformations(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }

    public boolean accepterCommande(Commande commande) {
        return true;
    }

    public boolean refuserCommande(Commande commande) {
        return true;
    }

    public void signalerProbleme(String description){}

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public List<Plat> getMenu() {
        return new ArrayList<>(menu);
    }

    public boolean isOuvert() {
        return ouvert;
    }

    public void setOuvert(boolean ouvert) {
        this.ouvert = ouvert;
    }
}
