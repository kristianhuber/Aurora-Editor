package aurora.world.sfx;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import aurora.util.AuroraEngine;

public class Camera {
	public static final float SCROLL_SPEED = 75F;
	public static final float SPEED = 50F;
	
	private Vector3f position = new Vector3f(0, 15, 0);
	private Vector3f rotation = new Vector3f(0, 0, 0);

	public void move() {

		float tick = AuroraEngine.getDelta();
		
		// Increases the View Right or Left
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {

			rotation.y += SCROLL_SPEED * tick;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {

			rotation.y -= SCROLL_SPEED * tick;
		}

		// Calculates the Change in Direction
		float dx = (float) (SPEED * Math.sin(Math.toRadians(rotation.y)));
		float dz = (float) (SPEED * Math.cos(Math.toRadians(rotation.y)));
		
		// Handles the Forward and Back Directions
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {

			position.z -= dz * tick;
			position.x += dx * tick;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {

			position.z += dz * tick;
			position.x -= dx * tick;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			
			position.x -= dz * tick;
			position.z -= dx * tick;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			
			position.x += dz * tick;
			position.z += dx * tick;
		}

		// Handles the Up and Down Directions
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {

			position.y += SPEED * tick;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {

			position.y -= SPEED * tick;
		}
	}

	public Vector3f getPosition() {

		return position;
	}

	public void setPosition(Vector3f position) {

		this.position = position;
	}

	public Vector3f getRotation() {

		return rotation;
	}
}
