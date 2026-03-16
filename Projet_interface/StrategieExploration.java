package entites;

public class StrategieExploration implements Strategie {

	@Override
	public void executerDeplacement(Joueur j, Grille g) {
	    Case actuelle = j.getCaseActuelle();
	    // On met les directions dans une liste pour pouvoir les mélanger
	    java.util.List<int[]> directions = java.util.Arrays.asList(
	        new int[]{0,1}, new int[]{0,-1}, new int[]{1,0}, new int[]{-1,0}
	    );
	    // MÉLANGE : évite d'aller toujours vers le bas par défaut
	    java.util.Collections.shuffle(directions);

	    Case meilleureCase = null;
	    int meilleureValeur = -1000;

	    for (int[] d : directions) {
	        Case voisine = g.getCases(actuelle.getX() + d[0], actuelle.getY() + d[1]);
	        if (voisine != null) {
	            int score = 0;

	            // 1. Priorité absolue aux bonus
	            if (voisine.getItem() != null) {
	                score += voisine.getItem().getEnergie() * 10; 
	            }

	            // 2. Bonus pour l'exploration (cases jamais vues)
	            if (!j.getPapier().contains(voisine)) {
	                score += 5; // Encourage à aller vers l'inconnu
	            } else {
	                score -= 5; // Décourage de revenir en arrière inutilement
	            }

	            if (score > meilleureValeur) {
	                meilleureValeur = score;
	                meilleureCase = voisine;
	            }
	        }
	    }

	    if (meilleureCase != null) {
	        j.setCaseActuelle(meilleureCase);
	    }
	}

}
