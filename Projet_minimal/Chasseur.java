
package entites;

public class Chasseur extends Joueur {
	private Bonhomme bonhomme;
    public Chasseur(int energie, Case caseActuelle) { super("Chasseur", energie, caseActuelle); }

    protected Case choisirDestination(Grille grille) {

        System.out.println("Tour du Chasseur.");
        System.out.println("Case actuelle : (" + getCaseActuelle().getX() + "," + getCaseActuelle().getY() + ")");

        // 1) Si le bonhomme est adjacent -> il le tue
        if (bonhomme != null && bonhomme.estVivant()) {
        	if (getCaseActuelle()== bonhomme.getCaseActuelle()) {
        		System.out.println("Le chasseur est adjacent au bonhomme : il le tue !");
        		bonhomme.mourir();
        	}

            if (estAdjacent(getCaseActuelle(), bonhomme.getCaseActuelle())) {
                System.out.println("Le chasseur est adjacent au bonhomme : il le tue !");
                Case caseBonhomme = bonhomme.getCaseActuelle();
                bonhomme.mourir(); 
                // Il ne bouge pas (mais perd quand même 1 énergie dans seDeplacer())
                return caseBonhomme;
            }
        }

        // 2) Sinon il va vers la cible
        Case cible = grille.getCible();

        if (cible == null) {
            System.out.println("Aucune cible définie.");
            return getCaseActuelle();
        }

        int x = getCaseActuelle().getX();
        int y = getCaseActuelle().getY();

        int cx = cible.getX();
        int cy = cible.getY();

        int newX = x;
        int newY = y;

        // Déplacement d'une case vers la cible
        if (cx > x) newX++;
        else if (cx < x) newX--;
        else if (cy > y) newY++;
        else if (cy < y) newY--;

        return grille.getCases(newX, newY);
    }

    public void attaquer(Joueur joueur) { 
        System.out.println("Le chasseur élimine le bonhomme !");
    }

}
