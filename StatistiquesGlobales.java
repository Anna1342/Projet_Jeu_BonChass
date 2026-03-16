package entites;

public class StatistiquesGlobales implements Statistique, java.io.Serializable{
	private static final long serialVersionUID = 1L; //sert à enregistrer les stats dans un fichier binaire interne a java
	
	 private int nombrePartie;
	 private int victoire;
	 private int bestScore = Integer.MAX_VALUE; /*On commence par mettre le meilleur score à la valeur la plus élevé possible 
	  pour s'assurer que la première partie définisse le nouveau meilleur score*/
	 private long tempsTotal = 0;

	 public void statistiquePartie(int nombreCoups, boolean gagner, long duree) {
		nombrePartie++;
	    if (gagner) {
	            victoire++;
	            if (nombreCoups < bestScore) {
	                bestScore = nombreCoups;
	            }
	        }
	    tempsTotal += duree;
	    }

	public int getNombrePartie() {
		return nombrePartie;
	}

	public double getTauxVictoire() {
	    return nombrePartie == 0 ? 0 : (double) victoire / nombrePartie * 100;
	    }

	public long getTempsJouer() {
		return tempsTotal;
	}

	public int getBestScore() {
		if(bestScore == Integer.MAX_VALUE) {
			return 255;
		}
		else return bestScore;
	}

	public void resetStats() {
		nombrePartie = 0;
		victoire = 0;
		bestScore=Integer.MAX_VALUE;
		tempsTotal = 0;
	}
}