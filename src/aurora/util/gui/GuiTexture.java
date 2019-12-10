package aurora.util.gui;

import org.lwjgl.util.vector.Vector2f;

import aurora.util.AuroraEngine;

public class GuiTexture {
	public static final int BOTTOM_RIGHT = 0;
	public static final int BOTTOM_LEFT = 1;
	public static final int TOP_RIGHT = 2;
	public static final int TOP_LEFT = 3;
	public static final int CENTER = 4;

	private int texture;
	private Vector2f position;
	private Vector2f scale;

	/* Constructor Method */
	public GuiTexture(int texture, float x, float y, float scale) {

		this(texture, new Vector2f(x, y), new Vector2f(scale, scale));
	}
	
	/* Constructor Method */
	public GuiTexture(int texture, Vector2f pos, float scale){
		
		this(texture, pos, new Vector2f(scale, scale));
	}
	
	/* Main Constructor Method */
	public GuiTexture(int texture, Vector2f pos, Vector2f scale){
		
		this.texture = texture;
		
		float x2 = (2 * pos.x / AuroraEngine.WIDTH) - (1 - scale.x);
		float y2 = (2 * pos.y / AuroraEngine.HEIGHT) - (1 - scale.y);
		
		this.position = new Vector2f(x2, y2);
		
		this.scale = scale;
	}
	
	/* Gets the Texture ID */
	public int getTexture() {

		return texture;
	}

	/* Sets the Texture ID */
	public void setTexture(int texture) {

		this.texture = texture;
	}

	/* Gets the Position On Screen */
	public Vector2f getPosition() {

		return position;
	}

	/* Sets the Position on Screen */
	public void setPosition(Vector2f position) {

		this.position = position;
	}

	/* Gets the Scale */
	public Vector2f getScale() {

		return scale;
	}

	/* Sets the Scale */
	public void setScale(Vector2f scale) {

		this.scale = scale;
	}

}
