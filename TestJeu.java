package test;
import entites.*;

public class TestJeu {
    public static void main(String[] args) {
        System.out.println("Test de la Classe Grille");

        // 1. Initialisation de la grille 
        int taille = 10;
        Grille grille = new Grille(taille);
        System.out.println("Grille créée avec une taille de : " + grille.getLongueurCote());
        
        // 2. Récupérer des cases spécifiques existantes via la matrice 
        //Case c1 = grille.getCases(2, 3); 
        //Case c2 = grille.getCases(4, 4);

        // 3. Création des joueurs avec ces cases
        // Joueur bonhomme = new Bonhomme(10, c1);
        // Joueur chasseur = new Chasseur(10, c2);
        
        //System.out.println("Le bonhomme est en : " + bonhomme.getCaseActuelle().getX() + "," + bonhomme.getCaseActuelle().getY());
        //System.out.println("Le chasseur est en : " + chasseur.getCaseActuelle().getX() + "," + chasseur.getCaseActuelle().getY());
        

        // 3. Test de placement des éléments, placerElements s'occupera de leur donner une case
        Joueur bonhomme = new Bonhomme(10, null); // On dit explicitement qu'il n'a pas encore de case
        Joueur chasseur = new Chasseur(1, null);
        
        Jeu jeu = new Jeu(grille, bonhomme, chasseur);      // création de l'objet
        jeu.debuterJeu();
        
        /*grille.placerElements(bonhomme, chasseur);
        System.out.println("Placement réussi !");
        
        // 4. Vérification des positions
    	Case posCible = grille.getCible();
        Case posB = bonhomme.getCaseActuelle();
        Case posC = chasseur.getCaseActuelle();

        System.out.println("Cible placée en : [" + posCible.getX() + ", " + posCible.getY() + "]");
        System.out.println("Bonhomme placé en : [" + posB.getX() + ", " + posB.getY() + "]");
        System.out.println("Chasseur placé en : [" + posC.getX() + ", " + posC.getY() + "]");
        
            // 5. Test de sécurité : personne ne doit être sur la même case
            if (posB == posC || posB == posCible || posC == posCible) {
                System.out.println("ERREUR : Superposition d'éléments !");
            } else {
                System.out.println("SUCCÈS : Tous les éléments sont sur des cases distinctes.");
        }
            System.out.println("--- Liste des Items sur la grille ---");
            for (int i = 0; i < grille.getLongueurCote(); i++) {
                for (int j = 0; j < grille.getLongueurCote(); j++) {
                    Case c = grille.getCases(i, j);
                    if (c.getItem() != null) {
                        System.out.println("Item (" + c.getItem().getValeur() + ") trouvé en : " + i + "," + j);
                    }
                }
            } 
            grille.afficher(bonhomme, chasseur); */
    }
}
