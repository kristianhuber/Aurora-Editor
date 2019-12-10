package aurora.world;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import aurora.util.gui.GuiTexture;
import aurora.util.gui.ingame.Inventory;

public class World {
	public static final float GRAVITY = 9.8F;
	private Sector[][] sectors;
	private Inventory i;
	private boolean showInventory = false;
	int time = 0;

	/* Constructor Method */
	public World(int size) {

		sectors = new Sector[size][size];
		sectors[0][0] = new Sector();
		i = new Inventory("inventory", GuiTexture.CENTER, new Vector2f(0.50F, 0.75F));
	}

	/* Updates the World */
	public void tick() {

		i.tick();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && time == 0) {

			if(showInventory){
			
				showInventory = false;
				i.clearScreen();
			}else{
				
				showInventory = true;
				i.drawInventory();
			}
			
			time = 10;
		}
		
		if(time > 0){
			time--;
		}

		sectors[0][0].tick();
	}

	/* Gets the Sector at given Coordinates */
	public Sector getSector(int x, int z) {

		return sectors[x][z];
	}
}
