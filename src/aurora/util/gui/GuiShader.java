package aurora.util.gui;

import org.lwjgl.util.vector.Matrix4f;

import aurora.world.sfx.Shader;

/**
 * @Author: Thin Matrix
 * @Description: Gui Shader
 * 
 */

public class GuiShader extends Shader {

	private static final String FRAGMENT_FILE = "util/gui/GuiFragmentShader.txt";
	private static final String VERTEX_FILE = "util/gui/GuiVertexShader.txt";

	private int location_transformationMatrix;

	/* Constructor Method */
	public GuiShader() {

		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/* Loads the Transformation Matrix */
	public void loadTransformation(Matrix4f matrix) {

		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/* Gets the Location of the Transformation Matrix */
	protected void getAllUniformLocations() {

		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
	}

	/* Writes the Position Attribute to the Shader */
	protected void bindAttributes() {

		super.bindAttribute(0, "position");
	}
}
