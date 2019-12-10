package aurora.world.entities;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import aurora.util.Calculator;
import aurora.world.sfx.Camera;
import aurora.world.sfx.Light;
import aurora.world.sfx.Shader;

/**
 * @Author: Thin Matrix
 * @Description: Shades the Entities
 * 
 */

public class StaticShader extends Shader {

	private static final String FRAGMENT_FILE = "world/entities/FragmentShader.txt";
	private static final String VERTEX_FILE = "world/entities/VertexShader.txt";
	private static final int MAX_LIGHTS = 4;

	private int loc_transformMat;
	private int loc_lightPos[];
	private int loc_lightCol[];
	private int loc_fakeLight;
	private int loc_projMat;
	private int loc_viewMat;
	private int loc_skyCol;

	/* Constructor Method */
	public StaticShader() {

		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/* Binds each VBO */
	protected void bindAttributes() {

		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/* Gets the Shader Variables */
	protected void getAllUniformLocations() {

		loc_transformMat = super.getUniformLocation("transformationMatrix");
		loc_fakeLight = super.getUniformLocation("useFakeLighting");
		loc_projMat = super.getUniformLocation("projectionMatrix");
		loc_viewMat = super.getUniformLocation("viewMatrix");
		loc_skyCol = super.getUniformLocation("skyColor");

		loc_lightPos = new int[MAX_LIGHTS];
		loc_lightCol = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {

			loc_lightPos[i] = super.getUniformLocation("lightPosition[" + i
					+ "]");

			loc_lightCol[i] = super.getUniformLocation("lightColor[" + i + "]");
		}
	}

	/* Sets the Transformation Matrix in the Shader */
	public void loadTransformationMatrix(Matrix4f matrix) {

		super.loadMatrix(loc_transformMat, matrix);
	}

	/* Sets the Projection Matrix in the Shader */
	public void loadProjectionMatrix(Matrix4f matrix) {

		super.loadMatrix(loc_projMat, matrix);
	}

	/* Sets the View Matrix in the Shader */
	public void loadViewMatrix(Camera camera) {

		Matrix4f viewMatrix = Calculator.createViewMatrix();

		super.loadMatrix(loc_viewMat, viewMatrix);
	}

	/* Tells the Shader to Use Fake Lighting */
	public void loadFakeLightingVariable(boolean useFake) {

		super.loadBoolean(loc_fakeLight, useFake);
	}

	/* Sets the Sky Color in the Sky Color to the Shader */
	public void loadSkyColor(float r, float g, float b) {

		super.loadVector(loc_skyCol, new Vector3f(r, g, b));
	}

	/* Loads the Light Variables to the Shader */
	public void loadLights(List<Light> lights) {

		for (int i = 0; i < MAX_LIGHTS; i++) {

			if (i < lights.size()) {
				
				super.loadVector(loc_lightPos[i], lights.get(i).getPosition());
				super.loadVector(loc_lightCol[i], lights.get(i).getColor());
			} else {

				super.loadVector(loc_lightPos[i], new Vector3f(0, 0, 0));
				super.loadVector(loc_lightCol[i], new Vector3f(0, 0, 0));
			}
		}
	}
}
