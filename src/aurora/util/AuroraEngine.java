package aurora.util;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import aurora.editor.Editor;
import aurora.world.World;
import aurora.world.sfx.Camera;
import aurora.world.sfx.MasterRenderer;

/**
 * @Author: Kristian H.
 * @Description: Base Class for the Engine
 * @Special_Thanks: Thin Matrix
 * 
 */

public class AuroraEngine {
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();
	public static final String TITLE = "Aurora Engine v1.0";

	public static enum RenderType {
		Game, Editor
	}

	public static RenderType rendering;
	public static Camera camera;
	public static World world;

	private static long lastFrameTime;
	private static float delta;
	
	/* Creates the Display and Loads Default Resources */
	public static void createDisplay() {

		// Creates the Display
		try {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.setResizable(true);
			Display.setTitle(TITLE);
			Display.sync(60);

			Display.create();

		} catch (LWJGLException e) {

			e.printStackTrace();
		}
	}

	/* Loads the Resources */
	public static void loadResourcesAndProperties() {

		// Sets the Properties
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		// Load Resources
		TextureManager.loadTexture("grass");
		TextureManager.loadTexture("snow");
		TextureManager.loadTexture("stone");
		TextureManager.loadTexture("sand");
		TextureManager.loadTexture("map");
		TextureManager.loadTexture("inventory", "gui");

		ModelManager.loadToVao("quad",
				new float[] { -1, 1, -1, -1, 1, 1, 1, -1 });

		world = new World(9);
		camera = new Camera();
		rendering = RenderType.Game;
	}

	/* Starts to Render */
	public static void startRendering() {

		if (Editor.errorFree) {

			Editor.writeOutput("Loaded Error Free...");
		}

		MasterRenderer.initialize();

		// Looping
		while (!Display.isCloseRequested()) {

			if (rendering == RenderType.Game) {

				MasterRenderer.render();
				camera.move();
				world.tick();

			} else if (rendering == RenderType.Editor) {

			}

			long currentFrameTime = getCurrentTime();
			delta = (currentFrameTime - lastFrameTime) / 1000F;
			lastFrameTime = currentFrameTime;
			
			Display.update();
		}

		AuroraEngine.cleanUp();
	}

	/* Cleans Up Resources */
	public static void cleanUp() {

		TextureManager.cleanUp();
		MasterRenderer.cleanUp();
		ModelManager.cleanUp();

		Display.destroy();
	}
	
	public static float getDelta() {

		return delta;
	}
	
	private static long getCurrentTime() {
		
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
