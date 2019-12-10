package aurora.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import aurora.util.gui.GuiTexture;
import aurora.world.sfx.Camera;

/**
 * @Author: Thin Matrix & Kristian
 * @Description: Does Math to Make Life Easier
 * 
 */

public class Calculator {

	/* Converts the Coordinates to OpenGL Coordinates */
	public static Vector2f convertToOpenGLCoords(int posX, int posY){
		
		float x = (2.0f * posX) / Display.getWidth() - 1f;
		float y = (2.0f * posY) / Display.getHeight() - 1f;
		
		return new Vector2f(x, y);
	}

	/* Helps with Coordinates */
	public static Vector2f coordinateHelper(int position, Vector2f scale) {
		float x = 0, y = 0;

		switch (position) {

		case GuiTexture.TOP_RIGHT:

			x = (1 - scale.x) * AuroraEngine.WIDTH;
			y = (1 - scale.y) * AuroraEngine.HEIGHT;
			break;
		case GuiTexture.TOP_LEFT:

			y = (1 - scale.y) * AuroraEngine.HEIGHT;
			break;
		case GuiTexture.BOTTOM_RIGHT:

			x = (1 - scale.x) * AuroraEngine.WIDTH;
			break;
		case GuiTexture.CENTER:
			
			x = (1 - scale.x) * AuroraEngine.WIDTH;
			x /= 2;
			y = (1 - scale.y) * AuroraEngine.HEIGHT;
			y /= 2;
			break;
		}

		return (new Vector2f(x, y));
	}

	/* Determines Terrain Collision Detection */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3,
			Vector2f pos) {

		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x)
				* (p1.z - p3.z);

		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x)
				* (pos.y - p3.z))
				/ det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x)
				* (pos.y - p3.z))
				/ det;
		float l3 = 1.0f - l1 - l2;

		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/* Creates a Transformation Matrix for 2D Objects */
	public static Matrix4f createTransformationMatrix(Vector2f translation,
			Vector2f scale) {

		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();

		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);

		return matrix;
	}

	/* Creates a TransformationMatrix for 3D objects */
	public static Matrix4f createTransformationMatrix(Vector3f translation,
			float rx, float ry, float rz, float scale) {

		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();

		Matrix4f.translate(translation, matrix, matrix);

		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0),
				matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0),
				matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1),
				matrix, matrix);

		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

		return matrix;
	}

	/* Creates a ViewMatrix for the Scene */
	public static Matrix4f createViewMatrix() {
		Camera camera = AuroraEngine.camera;

		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();

		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().x),
				new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().y),
				new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRotation().z),
				new Vector3f(0, 0, 1), matrix, matrix);

		Vector3f negativeCameraPos = new Vector3f(-camera.getPosition().x,
				-camera.getPosition().y, -camera.getPosition().z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);

		return matrix;
	}
}
