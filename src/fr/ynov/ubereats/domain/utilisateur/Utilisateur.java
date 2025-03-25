package fr.ynov.ubereats.domain.utilisateur;


public abstract class Utilisateur {
    protected String id;
    protected String nom;
    protected String email;
    protected String telephone;
    protected String motDePasse;
    protected String adresse;

    public Utilisateur(String id, String nom, String email, String telephone, String motDePasse, String adresse) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
    }

    public boolean seConnecter(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
    }

    public void seDeconnecter() {}

    public void modifierProfil(String nom, String email, String telephone, String adresse) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }
}
