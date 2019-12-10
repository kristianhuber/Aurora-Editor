package aurora.world.terrain;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import aurora.util.AuroraEngine;
import aurora.util.Calculator;
import aurora.util.ModelManager;
import aurora.world.entities.RawModel;
import aurora.world.sfx.Light;
import aurora.world.sfx.MasterRenderer;

/**
 * @Author: Thin Matrix
 * @Description: Renders the Terrain
 * 
 */

public class TerrainRenderer {

	private static TerrainShader shader = new TerrainShader();

	/* Loads the Projection Matrix */
	public static void loadProjectionMatrix() {

		shader.start();

		shader.loadProjectionMatrix(MasterRenderer.projectionMatrix);

		shader.connectTextureUnits();

		shader.stop();
	}

	/* Renders the Terrain */
	public static void render(Terrain terrain, ArrayList<Light> lights) {

		if (terrain != null) {
			// Loads Shader Settings
			shader.start();

			shader.loadLights(lights);
			shader.loadViewMatrix(AuroraEngine.camera);

			TerrainRenderer.prepareTerrain(terrain);
			TerrainRenderer.loadModelMatrix(terrain);

			// Renders the Terrain
			GL11.glDrawElements(
					GL11.GL_TRIANGLES,
					ModelManager.getModel(
							"terrain(" + terrain.getX() + "," + terrain.getZ()
									+ ")").getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);

			TerrainRenderer.unbindTexturedModel();

			// Stops the Shader
			shader.stop();
		}
	}

	/* Loads the Terrain's attributes */
	private static void prepareTerrain(Terrain terrain) {

		RawModel rawmodel = ModelManager.getModel("terrain(" + terrain.getX()
				+ "," + terrain.getZ() + ")");

		GL30.glBindVertexArray(rawmodel.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		bindTextures(terrain);
	}

	/* Loads the Textures to the Shader */
	private static void bindTextures(Terrain terrain) {

		TerrainTexturePack texturepack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturepack
				.getBackgroundTexture().getTextureID());

		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturepack.getrTexture()
				.getTextureID());

		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturepack.getgTexture()
				.getTextureID());

		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturepack.getbTexture()
				.getTextureID());

		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap()
				.getTextureID());
	}

	/* Resets the Shader's Memory */
	private static void unbindTexturedModel() {

		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	/* Loads the Transformation Matrix */
	private static void loadModelMatrix(Terrain terrain) {

		Matrix4f transformationMatrix = Calculator.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);

		shader.loadTransformationMatrix(transformationMatrix);
	}

	public static void cleanUp() {

		shader.cleanUp();
	}
}
