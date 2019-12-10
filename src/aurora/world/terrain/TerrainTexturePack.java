package aurora.world.terrain;

import aurora.util.TextureManager;

public class TerrainTexturePack {

	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;

	public TerrainTexturePack(String back, String r, String g, String b) {
		
		this.backgroundTexture = new TerrainTexture(TextureManager.getTextureID(back));
		this.rTexture = new TerrainTexture(TextureManager.getTextureID(r));
		this.gTexture = new TerrainTexture(TextureManager.getTextureID(g));
		this.bTexture = new TerrainTexture(TextureManager.getTextureID(b));
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public void setBackgroundTexture(TerrainTexture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public void setrTexture(TerrainTexture rTexture) {
		this.rTexture = rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public void setgTexture(TerrainTexture gTexture) {
		this.gTexture = gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}

	public void setbTexture(TerrainTexture bTexture) {
		this.bTexture = bTexture;
	}
}
