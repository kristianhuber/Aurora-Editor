package aurora.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import aurora.world.sfx.MasterRenderer;
import aurora.world.terrain.Terrain;

/**
 * @Author: Thin Matrix
 * @Description: Converts Mouse to World Space
 * 
 */

public class MousePointer {
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private static Matrix4f viewMatrix = Calculator.createViewMatrix();
	private static Vector3f currentRay = new Vector3f();
	private static Vector3f currentTerrainPoint;
	private static Terrain terrain;

	/* Sets the Current Terrain */
	public static void setTerrain(Terrain t) {

		terrain = t;
	}

	/* Gets the Current Terrain */
	public static Vector3f getCurrentTerrainPoint() {

		return currentTerrainPoint;
	}

	/* Updates the Point on the Screen */
	public static void update() {

		MousePointer.viewMatrix = Calculator.createViewMatrix();

		MousePointer.currentRay = calculateMouseRay();

		if (MousePointer.intersectionInRange(0, MousePointer.RAY_RANGE,
				MousePointer.currentRay)) {

			MousePointer.currentTerrainPoint = binarySearch(0, 0,
					MousePointer.RAY_RANGE, MousePointer.currentRay);
		} else {

			MousePointer.currentTerrainPoint = null;
		}
	}

	/* Calculates the Mouse Ray */
	private static Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		
		//Converts Coords to LWJGL Space
		Vector2f normalizedCoords = MousePointer.getNormalisedDeviceCoordinates(mouseX,
				mouseY);
		//Clips the Coordinates
		Vector4f clipCoords = new Vector4f(normalizedCoords.x,
				normalizedCoords.y, -1.0f, 1.0f);
		
		//Converts to Eye Coordinates
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		
		//Converts it to the World
		Vector3f worldRay = toWorldCoords(eyeCoords);
		
		return worldRay;
	}

	/* Converts Eye Coordinates to World Coordinates */
	private static Vector3f toWorldCoords(Vector4f eyeCoords) {
		
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		
		mouseRay.normalise();
		
		return mouseRay;
	}

	/* Converts the Clip Coordinates to Eye Coordinates */
	private static Vector4f toEyeCoords(Vector4f clipCoords) {
		
		Matrix4f invertedProjection = Matrix4f.invert(
				MasterRenderer.projectionMatrix, null);
		
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords,
				null);
		
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	/* Converts Java 2D Coordinate space to LWJGL Coordinate Space */
	private static Vector2f getNormalisedDeviceCoordinates(float mouseX,
			float mouseY) {
		
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		
		return new Vector2f(x, y);
	}

	/**
	 * Methods From Here on Take Care of the Terrain Intersection
	 */

	/* Converts the Camera Coordinates to a Ray */
	private static Vector3f getPointOnRay(Vector3f ray, float distance) {
		
		Vector3f camPos = AuroraEngine.camera.getPosition();
		
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance,
				ray.z * distance);
		
		return Vector3f.add(start, scaledRay, null);
	}

	/* Searches the Ray for an Intersection */
	private static Vector3f binarySearch(int count, float start, float finish,
			Vector3f ray) {
		
		//Gets the Half Way Point
		float half = start + ((finish - start) / 2f);
		
		//Loops Through the Amount of Times
		if (count >= RECURSION_COUNT) {
			
			Vector3f endPoint = getPointOnRay(ray, half);
			
			if (terrain != null) {
				
				return endPoint;
			} else {
				
				return null;
			}
		}
		
		//?
		if (intersectionInRange(start, half, ray)) {
			
			return binarySearch(count + 1, start, half, ray);
		} else {
			
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	/* Another Method that Sees if the Terrain is Above or Below Ground */
	private static boolean intersectionInRange(float start, float finish,
			Vector3f ray) {
		
		Vector3f startPoint = getPointOnRay(ray, start);
		
		Vector3f endPoint = getPointOnRay(ray, finish);
		
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			
			return true;
		} else {
			
			return false;
		}
	}

	/* Sees if the Terrain is Above or Below Ground */
	private static boolean isUnderGround(Vector3f testPoint) {
		
		float height = 0;
		
		if (terrain != null) {
			
			height = terrain.getHeightOfTerrain(testPoint.getX(),
					testPoint.getZ());
		}
		
		if (testPoint.y < height) {
			
			return true;
		} else {
			
			return false;
		}
	}
}
