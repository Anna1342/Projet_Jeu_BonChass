package entites;

import java.util.Random;


public class Grille {
    private int longueurCote; 
    private Case[][] cases; //tableau en 2D
    private Case cible;
    private Random rand = new Random();
    
    public int getLongueurCote() {
		return longueurCote;
	}
    
    public Grille(int longueurCote) {
        this.longueurCote = longueurCote;
        this.cases = new Case[longueurCote][longueurCote]; // Initialisation matrice
        for (int i = 0; i < longueurCote; i++) {  // Initialisation de toutes les cases vides
            for (int j = 0; j < longueurCote; j++) {
                cases[i][j] = new Case(i, j); //Remplissage
            }
        }
    }
    
    
    public Case getCases(int x, int y) {
    	if(x>=0 && x<longueurCote && y>=0 && y<longueurCote) {
    		return cases[x][y];
    	}
    	return null; //sécurité pour éviter de renvoyer des cases hors de la grille
    }

    public Case[][] getCases(){ // méthode qu'on utilise quasimment pas mais qu'on garde
    	return cases;
    }
    
	//Méthode pour configurer le plateau selon les règles de départ.
    public void placerElements(Joueur bonhomme, Joueur chasseur) {
    	// tirer x et y au sort entre 0 et longueur cote
    	int xCible = rand.nextInt(longueurCote);
    	int yCible = rand.nextInt(longueurCote);
        this.cible = cases[xCible][yCible]; 
        
        //Placer les items bonus/malus sur les cases restantes 
        genererItems();
         
        // Positionner le Bonhomme et le chasseur sur une case sans Item et diff de la cible et posB =/ POSc
        Case caseBonhomme = trouverCaseDepartValide(); 
        bonhomme.setCaseActuelle(caseBonhomme);

        Case caseChasseur; 
        do {
            caseChasseur = trouverCaseDepartValide();
        } while (caseChasseur == caseBonhomme); 
        chasseur.setCaseActuelle(caseChasseur);

    }
    
    //Trouve une case qui n'est pas la cible et qui n'a pas d'item.
    private Case trouverCaseDepartValide() {
        Case c;
        do {
        	// On tire deux indices au sort poue la matrice
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
                int valeur;
                    if (rand.nextDouble() < 0.66) {
                        valeur = -1; // 
                    } else {
                        valeur = 1;  // 
                    }
                
                Item nouvelItem = new Item(valeur);
                c.setItem(nouvelItem);
                
                itemsPlaces++;
            }
        }
    }

    public void setCible(Case cible) { // Méthode pour placer la cible (utilisée dans placerItems)
        this.cible = cible;
    }
    public Case getCible() { // Getter pour le Chasseur (et pour vérifier la fin de partie)
        return this.cible;
    }
    
    
    /* Méthode pour trouver cases adjacentes à partir des cases et du joueur? à voir si ça doit etre dans grille ou pas
 	public void trouverCasesAdjacentes(Case cases, Joueur joueur) {
 		
 	}
 	public int get energie return item.getvaleur()
 	une implication bien conçue doit etre fermée aux modifications et ouvertes aux extensions
 	cellcule
 	
 	*/
    public void afficher(Joueur bonhomme, Joueur chasseur) {

        System.out.println("\n=== PLATEAU DE JEU ===");
        System.out.print("   "); // espace pour aligner avec les numéros de lignes
        for (int x = 0; x < longueurCote; x++) {
            System.out.print(" " + x + " "); // même largeur que [ ]
        }
        System.out.println();

        // 🔹 Affichage de la grille avec numéros de lignes (y)
        for (int y = 0; y < longueurCote; y++) {

            // numéro de ligne à gauche
            System.out.print(" " + y + " ");

            for (int x = 0; x < longueurCote; x++) {

                Case c = cases[x][y];

                // 1) Priorité au Bonhomme
                if (bonhomme.estVivant() && bonhomme.getCaseActuelle() == c) {
                    System.out.print("[B]");
                }
                // 2) Le Chasseur
                else if (chasseur.estVivant() && chasseur.getCaseActuelle() == c) {
                    System.out.print("[C]");
                }
                // 3) La Cible
                else if (c == this.cible) {
                    System.out.print("[X]");
                }
                // 4) Les Items
                else if (c.getItem() != null) {
                    if (c.getItem().getValeur() > 0) {
                        System.out.print("[+]");
                    } else {
                        System.out.print("[-]");
                    }
                }
                // 5) Case vide
                else {
                    System.out.print("[ ]");
                }
            }

            System.out.println();
        }

        System.out.println("======================\n");
    }


 }
