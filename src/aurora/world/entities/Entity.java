package aurora.world.entities;

import org.lwjgl.util.vector.Vector3f;

import aurora.util.TextureManager;
import aurora.world.World;

/**
 * @Author: Kristian
 * @Description: Entity
 * 
 */

public class Entity {

	private ModelTexture texture;

	public Vector3f position;
	public Vector3f rotation;
	
	private float scale;

	private String ID;
	
	public World w;
	
	/* Constructor Method */
	public Entity(String ID, ModelTexture texture, Vector3f position, float scale, World w) {
		
		this.rotation = new Vector3f(0, 0, 0);
		
		this.position = position;
		
		this.texture = texture;
		
		this.scale = scale;

		this.ID = ID;
		
		this.w = w;
	}
	
	/* Constructor Method */
	public Entity(String ID, Vector3f position, float scale, World w){
		
		this.texture = new ModelTexture(TextureManager.getTextureID(ID));
		
		this.rotation = new Vector3f(0, 0, 0);
		
		this.position = position;
		
		this.scale = scale;
		
		this.ID = ID;
		
		this.w = w;
	}
	
	/* Updates the Entity */
	public void tick(){
		
		float terrainHeight = w.getSector(0, 0).getTerrain().getHeightOfTerrain(position.x, position.z);
		
		if(position.y < terrainHeight){
			
			position.y = terrainHeight;
		}
		
		if(position.y > terrainHeight){
			
			position.y -= World.GRAVITY;
		}
	}
	
	/* Increases the Position Quickly */
	public void increasePosition(float dx, float dy, float dz) {

		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	/* Increases the Rotation Quickly */
	public void increaseRotation(float dx, float dy, float dz) {

		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}
	
	public void setPosition(Vector3f v){
		
		this.position = v;
	}
	
	/* Gets the Texture */
	public ModelTexture getTexture(){
		
		return texture;
	}
	
	/* Sets the Texture */
	public void setTexture(ModelTexture tex){
		
		this.texture = tex;
	}

	/* Gets the Scale */
	public float getScale() {

		return scale;
	}

	/* Sets the Scale */
	public void setScale(float scale) {

		this.scale = scale;
	}

	/* Gets the Entity ID */
	public String getID() {

		return ID;
	}
}
