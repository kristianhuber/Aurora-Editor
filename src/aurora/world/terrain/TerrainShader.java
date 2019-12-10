package aurora.world.terrain;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import aurora.util.Calculator;
import aurora.world.sfx.Camera;
import aurora.world.sfx.Light;
import aurora.world.sfx.Shader;

/**
 * @Author: Thin Matrix
 * @Description: Shades the Terrain
 * 
 */

public class TerrainShader extends Shader {

	private static final String FRAGMENT_FILE = "world/terrain/FragmentShaderTerrain.txt";
	private static final String VERTEX_FILE = "world/terrain/VertexShaderTerrain.txt";

	private static final int MAX_LIGHTS = 4;

	private int loc_transformMat;
	private int loc_projectMat;
	private int loc_lightCol[];
	private int loc_lightPos[];
	private int loc_viewMat;
	private int loc_backTex;
	private int loc_skyCol;
	private int loc_rTex;
	private int loc_gTex;
	private int loc_bTex;
	private int loc_map;

	/* Constructor Method */
	public TerrainShader() {

		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/* Writes Attributes to the Shader */
	protected void bindAttributes() {

		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/* Gets the Variables' location in the Shader */
	protected void getAllUniformLocations() {

		loc_transformMat = super.getUniformLocation("transformationMatrix");
		loc_projectMat = super.getUniformLocation("projectionMatrix");
		loc_backTex = super.getUniformLocation("backgroundTexture");
		loc_viewMat = super.getUniformLocation("viewMatrix");
		loc_skyCol = super.getUniformLocation("skyColor");
		loc_rTex = super.getUniformLocation("rTexture");
		loc_gTex = super.getUniformLocation("gTexture");
		loc_bTex = super.getUniformLocation("bTexture");
		loc_map = super.getUniformLocation("blendMap");

		loc_lightCol = new int[MAX_LIGHTS];
		loc_lightPos = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {

			loc_lightCol[i] = super.getUniformLocation("lightColor[" + i + "]");
			loc_lightPos[i] = super.getUniformLocation("lightPosition[" + i
					+ "]");
		}
	}

	/* Sets the Textures for each Number */
	public void connectTextureUnits() {

		super.loadInt(loc_backTex, 0);

		super.loadInt(loc_rTex, 1);
		super.loadInt(loc_gTex, 2);
		super.loadInt(loc_bTex, 3);

		super.loadInt(loc_map, 4);
	}

	/* Loads the Transformation Matrix to the Shader */
	public void loadTransformationMatrix(Matrix4f matrix) {

		super.loadMatrix(loc_transformMat, matrix);
	}

	/* Loads the Projection Matrix to the Shader */
	public void loadProjectionMatrix(Matrix4f matrix) {

		super.loadMatrix(loc_projectMat, matrix);
	}

	/* Loads the View Matrix to the Shader */
	public void loadViewMatrix(Camera camera) {

		Matrix4f viewMatrix = Calculator.createViewMatrix();
		super.loadMatrix(loc_viewMat, viewMatrix);
	}

	/* Loads the Sky Color to the Shader */
	public void loadSkyColor(float r, float g, float b) {

		super.loadVector(loc_skyCol, new Vector3f(r, g, b));
	}

	/* Loads the Lights to the Shader */
	public void loadLights(List<Light> lights) {

		// Loops through each one
		for (int i = 0; i < MAX_LIGHTS; i++) {

			if (i < lights.size()) {

				super.loadVector(loc_lightPos[i], lights.get(i).getPosition());
				super.loadVector(loc_lightCol[i], lights.get(i).getColor());

				// Default Light
			} else {

				super.loadVector(loc_lightPos[i], new Vector3f(0, 0, 0));
				super.loadVector(loc_lightCol[i], new Vector3f(0, 0, 0));
			}

		}
	}
}
