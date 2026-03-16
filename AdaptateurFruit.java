package entites;

public class AdaptateurFruit implements Item {
	
	private Fruit fruit; //On défini un objet fruit de classe Fruit

	public AdaptateurFruit(Fruit fruit) {
		this.fruit = fruit;
	}
	
	@Override
	public int getEnergie() {
		return fruit.gainEnergie(); //Renvoie la valeur de l'énergie qui doit être ajouté au joueur
	}
}
