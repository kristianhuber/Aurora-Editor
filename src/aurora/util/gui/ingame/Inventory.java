package aurora.util.gui.ingame;

import org.lwjgl.util.vector.Vector2f;

public class Inventory extends Gui{
	
	public Inventory(String texture, int position, Vector2f scale) {

		super(texture, position, scale);
	}
	
	public void drawInventory() {
		
		this.show();
	}
	
	public void clearScreen(){
		
		this.hide();
	}
}
