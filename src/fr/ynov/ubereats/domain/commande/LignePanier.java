package fr.ynov.ubereats.domain.commande;


import fr.ynov.ubereats.domain.restaurant.Plat;

public class LignePanier {
    private Plat plat;
    private int quantite;
    private double prixUnitaire;
    private double prixTotal;

    public LignePanier(Plat plat, int quantite) {
        this.plat = plat;
        this.quantite = quantite;
        this.prixUnitaire = plat.getPrix();
        calculerPrixTotal();
    }

    public void calculerPrixTotal() {
        this.prixTotal = this.quantite * this.prixUnitaire;
    }

    public Plat getPlat() {
        return plat;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        calculerPrixTotal();
    }

    public void modifierQuantite(int nouvelleQuantite) {
        if (nouvelleQuantite >= 0) {
            this.quantite = nouvelleQuantite;
            calculerPrixTotal();
        } else {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
    }

    @Override
    public String toString() {
        return "LignePanier{" +
                "plat=" + plat.getNom() +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", prixTotal=" + prixTotal +
                '}';
    }
}

