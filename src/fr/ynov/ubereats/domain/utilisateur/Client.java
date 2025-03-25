package fr.ynov.ubereats.domain.utilisateur;

import fr.ynov.ubereats.domain.commande.Commande;
import fr.ynov.ubereats.domain.commande.Panier;
import fr.ynov.ubereats.domain.restaurant.Plat;
import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Client extends Utilisateur {
    private Panier panier;
    private List<Commande> historique;

    public Client(String id, String nom, String email, String telephone, String motDePasse, String adresse) {
        super(id, nom, email, telephone, motDePasse, adresse);
        this.panier = new Panier();
        this.historique = new ArrayList<>();
    }

    public List<Restaurant> consulterRestaurants() {
        return  new ArrayList<>();
    }

    public void ajouterAuPanier(Restaurant restaurant, Plat plat, int quantite) {
        panier.ajouterPlat(plat, quantite);
    }

    public Commande passerCommande() {
        String commandeId = "CMD-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
        Restaurant restaurant = panier.getRestaurant();
        Commande nouvelleCommande = new Commande(commandeId, this, restaurant);
        historique.add(nouvelleCommande);
        panier.vider();
        return nouvelleCommande;
    }

    public void noterCommande(Commande commande, int note) {}

    public Panier getPanier() {
        return panier;
    }

    public List<Commande> getHistorique() {
        return new ArrayList<>(historique);
    }

}
