package entites;

public class Bonhomme extends Joueur implements ConfigDeplacement {
	
    private Strategie strategieExplo = new StrategieExploration(); // On initialise la stratégie d'exploration

    public Bonhomme(String nom, int energie, Case caseActuelle) { 
        super(nom, energie, caseActuelle); 
    }
   
    @Override
    protected Case choisirDestination(Grille grille) {
        // STRATÉGIE AUTOMATIQUE (Energie > 15) 
        if (this.getEnergie() > HIGH_VALUE) {
            System.out.println("--- Mode Automatique ---");
            this.setStrategie(strategieExplo);
            this.executerDeplacementAutomatique(grille); // Déplacement réel du bonhomme sur la grille 
            
            // Maintenant que le déplacement a été effectué on renvoit la case sur lequel se trouve désormais le bonhomme
            return getCaseActuelle();
        }

        // STRATÉGIE INITIALE (Mode Manuel) 
        System.out.println("Tour de" + getNom() + "(Mode Manuel).");
        System.out.println("Case actuelle : (" + getCaseActuelle().getX() + "," + getCaseActuelle().getY() + ")");

        return getCaseActuelle();
    }
}