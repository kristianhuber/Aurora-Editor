package aurora.world.sfx;

import org.lwjgl.util.vector.Vector3f;

/**
 * @Author: Thin Matrix
 * @Description: Holds Light Attributes
 * 
 */

public class Light {
	private Vector3f position;
	private Vector3f color;

	/* Constructor Method */
	public Light(Vector3f position, Vector3f color) {

		this.position = position;
		this.color = color;
	}

	/* Sets the Light Position */
	public void setPosition(Vector3f position) {

		this.position = position;
	}

	/* Gets the Light Position */
	public Vector3f getPosition() {

		return position;
	}

	/* Sets the Light Color */
	public void setColor(Vector3f color) {

		this.color = color;
	}

	/* Gets the Light Color */
	public Vector3f getColor() {

		return color;
	}
}
