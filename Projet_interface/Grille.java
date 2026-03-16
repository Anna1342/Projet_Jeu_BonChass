
package entites;

import java.util.Random;

public class Grille {
    private int longueurCote; 
    private Case[][] cases; 
    private Case cible;
    private Random rand = new Random();
    
    public int getLongueurCote() {
        return longueurCote;
    }
    
    public Grille(int longueurCote) {
        this.longueurCote = longueurCote;
        this.cases = new Case[longueurCote][longueurCote]; 
        for (int i = 0; i < longueurCote; i++) {  
            for (int j = 0; j < longueurCote; j++) {
                cases[i][j] = new Case(i, j); 
            }
        }
    }
    
    public Case getCases(int x, int y) {
        if(x >= 0 && x < longueurCote && y >= 0 && y < longueurCote) {
            return cases[x][y];
        }
        return null; 
    }

    public void placerElements(Joueur bonhomme, Joueur chasseur) {
        int xCible = rand.nextInt(longueurCote);
        int yCible = rand.nextInt(longueurCote);
        this.cible = cases[xCible][yCible]; 
        
        genererItems();
         
        Case caseBonhomme = trouverCaseDepartValide(); 
        bonhomme.setCaseActuelle(caseBonhomme);

        Case caseChasseur; 
        do {
            caseChasseur = trouverCaseDepartValide();
        } while (caseChasseur == caseBonhomme); 
        chasseur.setCaseActuelle(caseChasseur);
    }
    
    private Case trouverCaseDepartValide() {
        Case c;
        do {
            int xRand = rand.nextInt(longueurCote);
            int yRand = rand.nextInt(longueurCote);
            c = cases[xRand][yRand];
        } while (c == this.cible || (c.getItem() != null));
        return c;
    }

    private void genererItems() {
        int nbItemsAPlacer = (longueurCote * longueurCote) / 5;
        int itemsPlaces = 0;
        
        while (itemsPlaces < nbItemsAPlacer) {
            int xItem = rand.nextInt(longueurCote);
            int yItem = rand.nextInt(longueurCote);
            Case c = cases[xItem][yItem];

            if (c != this.cible && c.getItem() == null) {
                double proba = rand.nextDouble();
                Item nouvelItem;

                if (proba < 0.40) { 
                    // 40% de chance d'avoir un Feu (Malus)
                    nouvelItem = new AdaptateurFeu(new Feu(5)); 
                } else if (proba < 0.70) { 
                    // 30% de chance d'avoir un Fruit (Bonus)
                    nouvelItem = new AdaptateurFruit(new Fruit(10));
                } else { 
                    // 30% de chance d'avoir un Predateur (Gros Malus)
                    nouvelItem = new AdaptateurPredateur(new Predateur(10));
                }
                
                c.setItem(nouvelItem);
                itemsPlaces++;
            }
        }
    }

    public Case getCible() { 
        return this.cible;
    }

    
    public void afficher(Joueur bonhomme, Joueur chasseur) {
        System.out.println("\n=== PLATEAU DE JEU ===");
        
        // 1) En-tête des colonnes (X)
        // On met 5 espaces au début pour compenser le numéro de ligne "00   "
        System.out.print("     "); 
        for (int i = 0; i < longueurCote; i++) {
            // %-3d : aligne à gauche sur 3 espaces (ex: "1  ", "10 ")
            System.out.printf("%-3d", i);
        }
        System.out.println();

        // 2) Affichage de la grille (Lignes Y)
        for (int y = 0; y < longueurCote; y++) {
            // Numéro de ligne à gauche : %2d sur 2 chiffres + 3 espaces
            System.out.printf("%2d   ", y);

            for (int x = 0; x < longueurCote; x++) {
                Case c = cases[x][y];
                String contenu = " "; // Case vide par défaut

                // Priorité d'affichage
                if (bonhomme.estVivant() && bonhomme.getCaseActuelle() == c) {
                    contenu = "B";
                } else if (chasseur.estVivant() && chasseur.getCaseActuelle() == c) {
                    contenu = "C";
                } else if (c == this.cible) {
                    contenu = "X";
                } else if (c.getItem() != null) {
                    Item item = c.getItem();
                    if (item instanceof AdaptateurFruit) contenu = "+";
                    else if (item instanceof AdaptateurFeu) contenu = "-";
                    else if (item instanceof AdaptateurPredateur) contenu = "=";
                }

                // Affiche [X] - chaque bloc fait exactement 3 caractères de large
                System.out.print("[" + contenu + "]");
            }
            System.out.println(); // Fin de la ligne
        }
        System.out.println("======================\n");
    }
}

