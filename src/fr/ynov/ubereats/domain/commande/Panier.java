package fr.ynov.ubereats.domain.commande;

import fr.ynov.ubereats.domain.restaurant.Plat;
import fr.ynov.ubereats.domain.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private List<LignePanier> lignes;
    private double prixTotal;
    private Restaurant restaurant;

    public Panier() {
        this.lignes = new ArrayList<>();
        this.prixTotal = 0.0;
    }

    public void ajouterPlat(Plat plat, int quantite) {
        if (restaurant == null || restaurant.equals(plat.getRestaurant())) {
            for (LignePanier ligne : lignes) {
                if (ligne.getPlat().equals(plat)) {
                    ligne.modifierQuantite(ligne.getQuantite() + quantite);
                    calculerPrixTotal();
                    return;
                }
            }

            LignePanier nouvelleLigne = new LignePanier(plat, quantite);
            lignes.add(nouvelleLigne);


            if (restaurant == null) {
                restaurant = plat.getRestaurant();
            }

            calculerPrixTotal();
        } else {
            throw new IllegalArgumentException("Impossible d'ajouter un plat d'un autre restaurant");
        }
    }

    public void modifierPlat(Plat plat, int quantite) {
        for (LignePanier ligne : lignes) {
            if (ligne.getPlat().equals(plat)) {
                if (quantite > 0) {
                    ligne.setQuantite(quantite);
                } else {
                    lignes.remove(ligne);
                }
                calculerPrixTotal();
                return;
            }
        }
        throw new IllegalArgumentException("Plat non trouvé dans le panier");
    }

    public void supprimerPlat(Plat plat) {
        lignes.removeIf(ligne -> ligne.getPlat().equals(plat));
        calculerPrixTotal();

        if (lignes.isEmpty()) {
            restaurant = null;
        }
    }

    public void vider() {
        lignes.clear();
        prixTotal = 0.0;
        restaurant = null;
    }

    public void calculerPrixTotal() {
        prixTotal = lignes.stream()
                .mapToDouble(LignePanier::getPrixTotal)
                .sum();
    }
    public List<LignePanier> getLignes() {
        return new ArrayList<>(lignes);
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
