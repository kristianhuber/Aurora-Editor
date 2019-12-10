package aurora.util.gui.ingame;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import aurora.util.AuroraEngine;
import aurora.util.Calculator;
import aurora.util.TextureManager;
import aurora.util.gui.GuiRenderer;
import aurora.util.gui.GuiTexture;

public class Gui {
	private GuiTexture texture;
	private Rectangle2D.Float area;
	private int texID = 0;
	private int activationKey = 0;
	private boolean visible = false;

	public Gui(String texture, int position, float scale) {

		this(texture, position, new Vector2f(scale, scale));
	}

	public Gui(String texture, int position, Vector2f scale) {

		Vector2f pos = Calculator.coordinateHelper(position, scale);

		area = new Rectangle2D.Float(pos.x, pos.y,
				scale.x * AuroraEngine.WIDTH, scale.y * AuroraEngine.HEIGHT);

		this.texture = new GuiTexture(TextureManager.getTextureID(texture),
				pos, scale);
	}

	public void show() {

		if (!visible) {
			GuiRenderer.addGui(texture);
			texID = GuiRenderer.getLastIndex();
			visible = true;
		}
	}

	public void hide() {

		if (visible) {
			GuiRenderer.removeGui(texID);
			visible = false;
		}
	}

	public boolean isMouseOver() {

		if (visible) {

			return area.contains(Mouse.getX(), Mouse.getY());
		}

		return false;
	}

	public void onClick() {

		this.hide();
	}

	public void tick() {

		if (Mouse.isButtonDown(0) && isMouseOver()) {
			this.onClick();
		}
	}

	public void setActivationKey(int key) {

		this.activationKey = key;
	}

	public int getActivationKey(int key) {

		return activationKey;
	}

	public float getX() {

		return area.x;
	}

	public float getY() {

		return area.y;
	}

	public Vector2f getPosition() {

		return new Vector2f(area.x, area.y);
	}

	public Rectangle2D.Float getArea() {

		return area;
	}
}
