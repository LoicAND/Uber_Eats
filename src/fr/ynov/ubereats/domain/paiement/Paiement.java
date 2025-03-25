package fr.ynov.ubereats.domain.paiement;

import fr.ynov.ubereats.domain.commande.Commande;

import java.util.Date;

public class Paiement {
    private String id;
    private double montant;
    private Date date;
    private MethodePaiement methode;
    private StatutPaiement statut;
    private Commande commande;

    public Paiement(String id, Commande commande, double montant, MethodePaiement methode) {
        this.id = id;
        this.commande = commande;
        this.montant = montant;
        this.methode = methode;
        this.date = new Date();
        this.statut = StatutPaiement.EN_ATTENTE;
    }

    public boolean effectuerPaiement() {
        if (this.statut == StatutPaiement.EN_ATTENTE) {
            this.statut = StatutPaiement.ACCEPTEE;
            return true;
        }
        return false;
    }

    public boolean annulerPaiement() {
        if (this.statut == StatutPaiement.ACCEPTEE || this.statut == StatutPaiement.EN_ATTENTE) {
            this.statut = StatutPaiement.REMBOURSEE;
            return true;
        }
        return false;
    }

    public String genereRecu(){
        return "Reçu de paiement\n" +
                "ID: " + id + "\n" +
                "Montant: " + montant + "\n" +
                "Date: " + date + "\n" +
                "Méthode: " + methode + "\n" +
                "Statut: " + statut;
    }

    public String getId() {
        return id;
    }

    public double getMontant() {
        return montant;
    }

    public Date getDate() {
        return date;
    }

    public MethodePaiement getMethode() {
        return methode;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public Commande getCommande() {
        return commande;
    }
}


