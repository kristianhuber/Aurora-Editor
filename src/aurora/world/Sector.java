package aurora.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import aurora.world.entities.Entity;
import aurora.world.sfx.Light;
import aurora.world.terrain.Terrain;

public class Sector {

	private HashMap<String, List<Entity>> entities = new HashMap<String, List<Entity>>();
	private ArrayList<Light> lights = new ArrayList<Light>();
	private Terrain terrain;
	
	public Sector(){
		
		lights.add(new Light(new Vector3f(100, 100, 100), new Vector3f(1.75F, 1.25F, 0)));
	}
	
	/* Updates the Sector */
	public void tick(){
		
		for(String s : entities.keySet()){
			
			for(Entity e : entities.get(s)){
				
				e.tick();
			}
		}
	}
	
	public void addLight(Light l){
		
		lights.add(l);
	}
	
	public void removeLight(Light l){
		
		lights.remove(l);
	}
	
	public ArrayList<Light> getLights(){
		
		return lights;
	}
	
	/* Returns the Terrain */
	public Terrain getTerrain(){
		
		return terrain;
	}
	
	/* Sets the Terrain */
	public void setTerrain(Terrain t){
		
		this.terrain = t;
	}
	
	/* Returns the List of Entities in the World [WILL BE CHANGED IN THE SECTOR CLASS] */
	public HashMap<String, List<Entity>> getEntityList(){
		
		return entities;
	}
	
	/* Adds and Entity to the List to Render */
	public void addEntity(Entity entity) {
		
		List<Entity> batch = entities.get(entity.getID());

		if (batch != null) {
			
			batch.add(entity);
		} else {
			
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			
			entities.put(entity.getID(), newBatch);
		}
	}
}
