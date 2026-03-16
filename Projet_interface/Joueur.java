package entites;

import java.util.ArrayList;
import java.util.List;

public abstract class Joueur {
    private String nom;
    protected int energie; 
    private boolean vivant = true; 
    private Case caseActuelle; 
    protected Strategie strategie; // On la met en protected pour que les classes enfants "Bonhomme" et "Chasseur" y accèdent (seul le bonhomme en a réellement besoin)
    protected List<Case> papier = new ArrayList<>(); //Crée une liste des cases que le joueur a visité, également mise en protected pour que le "Bonhomme" y accède
    private List<Observateur> observateurs = new ArrayList<>();

    public void ajouterObservateur(Observateur obs) {
        observateurs.add(obs);
    }

    public void notifierObservateurs() {
        for (Observateur obs : observateurs) {
            obs.actualiser();
        }
    }

    public Joueur(String nom, int energie, Case caseActuelle) {
        this.nom = nom;
        this.energie = energie;
        this.caseActuelle = caseActuelle;
        // On ajoute la case de départ au papier dès le début
        this.papier.add(caseActuelle);
    }

    // --- GETTERS ET SETTERS ---
    public String getNom() { return nom; }
    public Case getCaseActuelle() { return caseActuelle; }
    public void setCaseActuelle(Case caseActuelle) { this.caseActuelle = caseActuelle; }
    public int getEnergie() { return energie; }
    public void setEnergie(int energie) { this.energie = energie; }
    public void setVivant(boolean vivant) { this.vivant = vivant; }
    
    // Getter pour le papier (utilisé par la stratégie exploration)
    public List<Case> getPapier() {
        return papier;
    }

    public void mourir() {
        vivant = false;
        System.out.println(nom + " est éliminé !");
    }

    public boolean estVivant() { 
        return vivant && energie > 0; 
    }

    // Méthode de déplacement de base (utilisée pour le mode manuel ou le Chasseur)
    public void seDeplacer(Grille grille) {
        if (!estVivant()) {
            System.out.println(nom + " est mort, il ne peut pas se déplacer.");
            return;
        }

        Case depart = caseActuelle;
        Case destination = choisirDestination(grille); 

        // Validation du déplacement
        if (destination != null && estDansLaGrille(grille, destination) && estAdjacent(depart, destination)) {
            // Mise à jour de l'historique (le papier)
            if (!papier.contains(destination)) {
                papier.add(destination);
            }
            
            // Effet de la case et mouvement
            destination.appliquerEffet(this);
            energie -= 1;
            caseActuelle = destination;
            
            System.out.println("Energie restante : " + energie);
            if (estVivant()) {
                System.out.println(nom + " se déplace en (" + caseActuelle.getX() + "," + caseActuelle.getY() + ")");
            }
        } else {
            System.out.println("Déplacement impossible ou destination invalide.");
            notifierObservateurs();
        
        }
    }

    // Utilisé pour déclencher la stratégie automatique (Design Pattern Strategy)
    public void executerDeplacementAutomatique(Grille grille) {
        if (this.strategie != null && estVivant()) {
            // On passe 'this' (le joueur actuel) et la grille
            this.strategie.executerDeplacement(this, grille);
        }
    }

    protected abstract Case choisirDestination(Grille grille);
    
    public void appliquerItem(Item i) { 
        if (i != null) {
            this.energie += i.getEnergie();
        }  
        if (this.energie <= 0) mourir(); 
    }

    protected boolean estDansLaGrille(Grille grille, Case c) {
        int x = c.getX();
        int y = c.getY();
        return x >= 0 && x < grille.getLongueurCote()
            && y >= 0 && y < grille.getLongueurCote();
    }

    protected boolean estAdjacent(Case a, Case b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx + dy) == 1;
    }
    
    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }
}
