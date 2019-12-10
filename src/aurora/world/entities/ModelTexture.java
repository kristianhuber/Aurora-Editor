package aurora.world.entities;

/**
 * @Author: Thin Matrix
 * @Description: Holds Attributes for Model's Textures
 * 
 */

public class ModelTexture {

	private boolean useFakeLighting = false;
	private boolean hasTransparency = false;
	
	private int textureID;
	
	/* Constructor Method */
	public ModelTexture(int id) {

		this.textureID = id;
	}
	
	/* Returns if the Texture Uses Fake Lighting */
	public boolean isUseFakeLighting() {
		
		return useFakeLighting;
	}

	/* Sets the Use of Fake Lighting */
	public void setUseFakeLighting(boolean useFakeLighting) {
		
		this.useFakeLighting = useFakeLighting;
	}

	/* Returns if the Texture Has Transparency */
	public boolean isHasTransparency() {
		
		return hasTransparency;
	}

	/* Sets if the Texture Has Transparency */
	public void setHasTransparency(boolean hasTransparency) {
		
		this.hasTransparency = hasTransparency;
	}

	/* Returns the Texture ID */
	public int getTextureID() {

		return textureID;
	}
}
