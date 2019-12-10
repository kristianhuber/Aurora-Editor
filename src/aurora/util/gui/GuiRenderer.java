package aurora.util.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import aurora.util.Calculator;
import aurora.util.ModelManager;
import aurora.world.entities.RawModel;

/**
 * @Author: Thin Matrix
 * @Description: Renders the Guis
 * 
 */

public class GuiRenderer {
	private static List<GuiTexture> guis = new ArrayList<GuiTexture>();

	private static final RawModel Quad = ModelManager.getModel("quad");
	private static final GuiShader shader = new GuiShader();

	/* Renders the GUIs */
	public static void render() {

		shader.start();

		// Starts the Conditions
		GL30.glBindVertexArray(Quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		// Actually Draws the Textures
		for (GuiTexture gui : guis) {

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			
			Matrix4f matrix = Calculator.createTransformationMatrix(
					gui.getPosition(), gui.getScale());

			shader.loadTransformation(matrix);

			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, Quad.getVertexCount());
		}

		// Disables Special Conditions
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.stop();
	}

	/* Removes a Gui from the Render List */
	public static void removeGui(int g) {

		guis.remove(g);
	}

	/* Adds a Gui to the Render List */
	public static void addGui(GuiTexture g) {

		guis.add(g);
	}
	
	/* Gets the Last Index */
	public static int getLastIndex(){
		
		return guis.size() - 1;
	}

	/* Cleans Up the Shader */
	public static void cleanUp() {

		shader.cleanUp();
	}
}
