package entites;

public class AdaptateurFeu implements Item {
	
    private Feu feu; //On défini un objet feu de classe Feu
    
    public AdaptateurFeu(Feu feu) { 
    	this.feu = feu; 
    	}
    
    @Override
    public int getEnergie() { 
		return -feu.energieMoins(); //Renvoie la valeur de l'énergie qui doit être retranché au joueur
	}
}
