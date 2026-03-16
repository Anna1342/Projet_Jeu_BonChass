package entites;

import java.util.Scanner;

public class Bonhomme extends Joueur {
    public Bonhomme(int energie, Case caseActuelle) { super("Bonhomme", energie, caseActuelle); }

    protected Case choisirDestination(Grille grille) {
        System.out.println("Tour du Bonhomme.");
        System.out.println("Case actuelle : (" + getCaseActuelle().getX() + "," + getCaseActuelle().getY() + ")");

        Scanner sc = new Scanner(System.in); // un seul scanner suffit

        System.out.print("Entrez l'abscisse de la nouvelle case (x): ");
        int x = sc.nextInt();

        System.out.print("Entrez l'ordonnée de la nouvelle case (y): ");
        int y = sc.nextInt();

        Case destination = grille.getCases(x, y);
        if (destination == null) {
            System.out.println("Coordonnées invalides !");
            return getCaseActuelle();
        }
        return destination;
    }
    
}


