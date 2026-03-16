
package entites;

public class Case {
    private int x; 
    private int y; 
    private Item item; 

    public Case(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void appliquerEffet(Joueur joueur) { 
        if (this.item != null) {
            joueur.appliquerItem(this.item);
            // Les bonus ne sont pas renouvelables 
            if (this.item.getEnergie() >0) {
                this.item = null;
            }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setItem(Item item) { 
    	this.item = item; }

	public Item getItem() {
		return this.item;
	}
}
