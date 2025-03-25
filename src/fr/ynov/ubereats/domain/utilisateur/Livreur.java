package fr.ynov.ubereats.domain.utilisateur;

import fr.ynov.ubereats.domain.commande.Commande;
import fr.ynov.ubereats.domain.commande.StatutLivreur;

import java.util.ArrayList;
import java.util.List;

public class Livreur extends Utilisateur {
    private String vehicule;
    private StatutLivreur statut;
    private List<Commande> livraisons;

    public Livreur(String id, String nom, String email, String telephone, String motDePasse, String adresse, String vehicule) {
        super(id, nom, email, telephone, motDePasse, adresse);
        this.vehicule = vehicule;
        this.statut = StatutLivreur.DISPONIBLE;
        this.livraisons = new ArrayList<>();
    }

    public boolean accepterLivraison(Commande commande) {
        if (this.statut == StatutLivreur.DISPONIBLE) {
            this.livraisons.add(commande);
            this.statut = StatutLivreur.EN_LIVRAISON;
            return true;
        }
        return false;
    }

    public boolean annulerLivraison(Commande commande) {
        if (this.statut == StatutLivreur.EN_LIVRAISON) {
            this.livraisons.remove(commande);
            this.statut = StatutLivreur.DISPONIBLE;
            return true;
        }
        return false;
    }

    public boolean terminerLivraison(Commande commande) {
        if (this.statut == StatutLivreur.EN_LIVRAISON && this.livraisons.contains(commande)) {
            this.livraisons.remove(commande);
            this.statut = StatutLivreur.DISPONIBLE;
            return true;
        }
        return false;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public StatutLivreur getStatut() {
        return statut;
    }

    public List<Commande> getLivraisons() {
        return new ArrayList<>(livraisons);
    }
}
