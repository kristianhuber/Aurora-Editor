package aurora.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import aurora.editor.Editor;

/**
 * @Author: Kristian
 * @Description: Loads all the Textures
 * 
 */

public class TextureManager {
	private static HashMap<String, Integer> TextureIDs = new HashMap<String, Integer>();

	public static final String TYPE_TEXTURE = "textures";
	public static final String TYPE_GUI = "gui";

	/* Registers A Texture Into Memory */
	public static void loadTexture(String fileName) {

		TextureManager.loadTexture(fileName, TextureManager.TYPE_TEXTURE);
	}

	/* Registers A Texture Into Memory */
	public static void loadTexture(String fileName, String type) {

		// Tries to Load the Texture
		Texture texture = null;
		try {

			texture = TextureLoader.getTexture("PNG", TextureManager.class
					.getResourceAsStream("/aurora/resources/" + type + "/"
							+ fileName + ".png"));

			// Registers the ID
			int textureID = texture.getTextureID();
			TextureIDs.put(fileName, textureID);
			
		} catch (Exception e) {

			Editor.errorFree = false;
			Editor.writeError("Could Not Load Texture for " + fileName);
			Editor.write("/aurora/resources/" + type + "/" + fileName + ".png");
		}
	}

	/* Registers A Texture Into Memory */
	public static void registerTexture(String path, String fileName) {

		File start = new File(path);
		File end = new File(Editor.getFilePath() + "/textures/" + fileName
				+ ".png");

		try {

			InputStream input = new FileInputStream(start);
			OutputStream output = new FileOutputStream(end);

			byte[] buf = new byte[1024];

			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {

				output.write(buf, 0, bytesRead);
			}

			input.close();
			output.close();
		} catch (Exception e) {

			Editor.errorFree = false;
			Editor.write("[Console]: An Error Occured When the Texture Was Loaded");
		}

	}

	/* Returns A Texture ID */
	public static int getTextureID(String fileName) {

		return TextureIDs.get(fileName);
	}

	public static Set<String> getAllTextureNames(){
		
		return TextureIDs.keySet();
	}
	
	/* Deletes Old Textures */
	public static void cleanUp() {

		for (Integer i : TextureIDs.values()) {

			GL11.glDeleteTextures(i);
		}
	}
}
