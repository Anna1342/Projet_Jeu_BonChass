package entites;

public interface Statistique {
	 void statistiquePartie(int nombreCoups, boolean gagner, long duree);
	 int getNombrePartie();
	 double getTauxVictoire();
	 long getTempsJouer(); //Temps total passé à jouer
	 int getBestScore(); //Victoire avec le plus petit nombre de coup 
	 void resetStats();
}
