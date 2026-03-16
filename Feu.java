package entites;

public class Feu {
	private int malus;

	public Feu(int malus) {
		super();
		this.malus = malus;
	}

	public int energieMoins() {
		return malus;
	}
}

