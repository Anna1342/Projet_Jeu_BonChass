package entites;

public class AdaptateurPredateur implements Item {
	
	private Predateur predateur; //On défini un objet predateur de classe Predateur

	public AdaptateurPredateur(Predateur predateur) {
		this.predateur = predateur;
	}

	@Override
	public int getEnergie() {
		return -predateur.perteEnergie(); //Renvoie la valeur de l'énergie qui doit être retranché au joueur
	}
}