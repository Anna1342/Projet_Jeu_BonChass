package entites;

public abstract class Joueur {
    private String nom;
    protected int energie; 
    private boolean vivant = true; 
    private Case caseActuelle; 

    public Joueur(String nom, int energie, Case caseActuelle) {
        this.nom = nom;
        this.energie = energie;
        this.caseActuelle = caseActuelle;
    }

	public String getNom() {
		return nom;
	}

	public void setCaseActuelle(Case caseActuelle) {	
		this.caseActuelle = caseActuelle;
	}
	
	public Case getCaseActuelle() {
		return caseActuelle;
	}
    
	public int getEnergie() {
		return energie;
	}

	public void setEnergie(int energie) {
		this.energie = energie;
	}

	public void setVivant(boolean vivant) {
		this.vivant = vivant;
	}
       
    public void mourir() {
        vivant = false;
        System.out.println(nom + " est éliminé !");
    }

    public boolean estVivant() { 
    	return vivant && energie > 0; 
    	}

    // Méthode commune : déplacement complet
    public void seDeplacer(Grille grille) {

        if (!estVivant()) {
            System.out.println(nom + " est mort, il ne peut pas se déplacer.");
            return;
        }

        Case depart = caseActuelle;
        Case destination;

        while (true) {
        	destination = choisirDestination(grille);
        	
            if (destination == null) {
                System.out.println("Destination nulle. Réessayez.");
                continue;
            }

            if (!estDansLaGrille(grille, destination)) {
                System.out.println("Déplacement impossible : hors limites. Réessayez.");
                continue;
            }

            if (!estAdjacent(depart, destination)) {
                System.out.println("Déplacement impossible : case non adjacente. Réessayez.");
                continue;
            }

            // Si tout est bon -> on sort de la boucle
            break;
        }
        
        // 1) Deplacement réel
        depart.appliquerEffet(this);
        energie -= 1;
        caseActuelle = destination;
        System.out.println("Energie restante : " + energie);
        if (estVivant()) {
        	System.out.println(nom + " se déplace en (" + caseActuelle.getX() + "," + caseActuelle.getY() + ")");
        }
    }

    
    // Bonhomme et Chasseur définissent leurs manière de choisir leur destination à travers cette fonction
    protected abstract Case choisirDestination(Grille grille);
    
    public void appliquerItem(Item i) { 
    	if (i != null) {
    	        this.energie += i.getValeur();
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
	
}

