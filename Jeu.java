package entites;

public class Jeu {
    private Grille grille; 
    private boolean gameOver;
    private int tour; 
    private Joueur bonhomme; // Ces variables sont vides par défaut
    private Joueur chasseur;

    // LE PROBLÈME EST ICI : Il faut remplir bonhomme et chasseur !
    public Jeu(Grille grille, Joueur bonhomme, Joueur chasseur) {
        this.grille = grille;
        this.bonhomme = bonhomme; // On lie le bonhomme créé dans le test à la classe Jeu
        this.chasseur = chasseur; // On lie le chasseur créé dans le test à la classe Jeu
        this.gameOver = false;
        this.tour = 0;
    }
    
    public void debuterJeu() { 
        // Initialisation
        grille.placerElements(bonhomme, chasseur);
        grille.afficher(bonhomme, chasseur);
        
        System.out.println("La partie commence !");
        
        // Boucle principale
        while (!gameOver) {
            tour++;
            Joueur joueurActif = getJoueurCourant(); 
            System.out.println("Tour " + tour + " : au tour de " + joueurActif.getNom());
            if (joueurActif.estVivant()== true) {
            	grille.afficher(bonhomme, chasseur);
            }
            joueurActif.seDeplacer(grille);
            verifierConditionsFin();
            // Sécurité pour ne pas boucler à l'infini tant que tes méthodes sont vides
            if (tour >= 40) gameOver = true; 
        }
    }
    private Joueur getJoueurCourant() {
        return (tour % 2 != 0) ? bonhomme : chasseur;
    }

    private void verifierConditionsFin() {
        // Ta logique de fin
        if (bonhomme.getCaseActuelle() == grille.getCible()) {
            gameOver = true;
            grille.afficher(bonhomme, chasseur);
            System.out.println("Félicitation vous avez gagné !!!" );
        }
        if (chasseur.getCaseActuelle() == grille.getCible()) {
           gameOver = true;
           grille.afficher(bonhomme, chasseur);
           System.out.println("Oh non... le chasseur a atteint la cible, vous avez perdu !" );    
        }
           
         if (bonhomme.estVivant() == false && chasseur.estVivant() == false) {
            gameOver = true;
            grille.afficher(bonhomme, chasseur);
            System.out.println("Les deux joueurs sont éliminés, c'est une égalité !" );   
        }
    }
}