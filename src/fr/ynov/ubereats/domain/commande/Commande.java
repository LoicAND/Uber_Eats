package fr.ynov.ubereats.domain.commande;


import fr.ynov.ubereats.domain.restaurant.Restaurant;
import fr.ynov.ubereats.domain.utilisateur.Client;
import fr.ynov.ubereats.domain.utilisateur.Livreur;

import java.util.Date;
import java.util.List;

public class Commande {
    private String id;
    private Client client;
    private Restaurant restaurant;
    private Livreur livreur;
    private List<LignePanier> lignes;
    private double prixTotal;
    private double fraisLivraison;
    private StatutCommande statut;
    private Date dateCreation;
    private Date dateLivraison;
    private String adresseLivraison;


    public Commande(String id, Client client, Restaurant restaurant) {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.dateCreation = new Date();
        this.statut = StatutCommande.CREE;
    }

    public double calculerPrixTotal() {
        return 0.0;
    }

    public boolean annuler(){
        if (this.statut == StatutCommande.CREE || this.statut == StatutCommande.ACCEPTEE) {
            this.statut = StatutCommande.ANNULEE;
            return true;
        }
        return false;
    }

    public boolean modifierStatut(StatutCommande nouveauStatut) {
        if (estTransitionValide(nouveauStatut)) {
            this.statut = nouveauStatut;

            if (nouveauStatut == StatutCommande.LIVREE) {
                this.dateLivraison = new Date();
            }
            return true;
        }
        return false;
    }

    private boolean estTransitionValide(StatutCommande nouveauStatut) {
        switch (this.statut) {
            case CREE:
                return nouveauStatut == StatutCommande.ACCEPTEE ||
                        nouveauStatut == StatutCommande.ANNULEE;
            case ACCEPTEE:
                return nouveauStatut == StatutCommande.EN_PREPARATION ||
                        nouveauStatut == StatutCommande.ANNULEE;
            case EN_PREPARATION:
                return nouveauStatut == StatutCommande.EN_LIVRAISON ||
                        nouveauStatut == StatutCommande.ANNULEE;
            case EN_LIVRAISON:
                return nouveauStatut == StatutCommande.LIVREE ||
                        nouveauStatut == StatutCommande.ANNULEE;
            case LIVREE:
                return false;
            case ANNULEE:
                return false;
            default:
                return false;
        }
    }

    public StatutCommande getStatut() {
        return statut;
    }

}

