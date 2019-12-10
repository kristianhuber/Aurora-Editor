package aurora.editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

import aurora.util.AuroraEngine;
import aurora.util.TextureManager;
import aurora.world.terrain.Terrain;
import aurora.world.terrain.TerrainTexture;
import aurora.world.terrain.TerrainTexturePack;

/**
 * @Author: Kristian
 * @Description: Window for Editing the World
 * 
 */

public class Editor extends JFrame {
	private static final long serialVersionUID = 1L;

	// Colors and Fonts for Swing
	public static Font editFont = new Font("Arial", Font.PLAIN, 12);
	public static Color background = new Color(10, 10, 10);
	public static Color foreground = new Color(255, 120, 0);
	public static Color component = new Color(background.getRed() + 10,
			background.getGreen() + 10, background.getBlue() + 10);
	public static Color selected = new Color(component.getRed() + 20,
			component.getGreen() + 20, component.getBlue() + 20);

	// Random Variables
	public static boolean errorFree = true;

	private static String projectName = "";
	private static String filePath = "";

	// Swing Components
	public static FileBrowserComponent browser = new FileBrowserComponent();
	public static ConsoleComponent console = new ConsoleComponent();
	public static OutputComponent output = new OutputComponent();
	public static MenuComponent menu = new MenuComponent();
	public static Canvas win = new Canvas();

	/* Constructor */
	public Editor() {

		// Sets Up the JFrame
		this.setSize(AuroraEngine.WIDTH, AuroraEngine.HEIGHT - 50);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(new EditorListener(this));
		this.getContentPane().setBackground(background);
		this.setMinimumSize(new Dimension(650, 500));
		this.setTitle(AuroraEngine.TITLE);
		this.setLayout(null);
		
		// /Sets up Swing Components
		browser.setBounds(5, 30, 225, 425);
		this.add(browser);

		menu.setBounds(0, 0, 895, 25);
		this.add(menu);

		output.setBounds(5, 460, 885, 160);
		this.add(output);

		output.addOutputTab("Console");
		output.getJPanel("Console").add(console);
		output.addOutputTab("Properties");

		win.setBounds(235, 30, 655, 425);
		this.add(win);

		// Renders
		this.setVisible(true);
		this.loadLWJGL();
	}

	/* Resizes the Swing Components */
	public class EditorListener implements ComponentListener {
		JFrame f;

		public EditorListener(JFrame f) {
			this.f = f;
		}

		public void componentHidden(ComponentEvent c) {
		}

		public void componentMoved(ComponentEvent c) {
		}

		public void componentShown(ComponentEvent c) {
		}

		public void componentResized(ComponentEvent c) {

			menu.setBounds(0, 0, f.getWidth() - 8, 25);

			browser.setBounds(5, 30, f.getWidth() / 4,
					(int) (f.getHeight() * 0.65));

			win.setBounds((browser.getX() + browser.getWidth() + 5), 30,
					f.getWidth() - (browser.getWidth() + browser.getX()) - 15,
					browser.getHeight());

			output.setBounds(5, browser.getHeight() + 35, f.getWidth() - 15,
					f.getHeight() - (browser.getHeight() + 65));
		}
	}

	/* Loads the LWJGL Display */
	public void loadLWJGL() {

		try {
			Display.setParent(win);
			Display.create();

			AuroraEngine.loadResourcesAndProperties();

			Terrain t = new Terrain("height", 0, -1, new TerrainTexturePack(
					"grass", "snow", "stone", "sand"), new TerrainTexture(
					TextureManager.getTextureID("map")));

			AuroraEngine.world.getSector(0, 0).setTerrain(t);

			AuroraEngine.startRendering();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Writes to the Console a Plain Color */
	public static void write(String str) {

		console.writeToConsole(str);
	}

	/* Writes to the Console in a Custom Color */
	public static void write(String str, String color) {

		console.writeToConsole(str, color);
	}

	/* Writes Output in Lime */
	public static void writeOutput(String str) {

		console.writeToConsole("[Console]:" + str, "lime");
	}

	/* Writes an Error */
	public static void writeError(String str) {

		console.writeToConsole("[Console]:" + str, "red");
	}

	/* Entry Method */
	public static void main(String[] args) {

		new Editor();
	}

	/* Sets the Project Name */
	public static void setProjectName(String s) {

		projectName = s;
	}

	/* Gets the Project Name */
	public static String getProjectName() {

		return projectName;
	}

	/* Sets the File Path */
	public static void setFilePath(String s) {

		filePath = s;
	}

	/* Gets the File Path */
	public static String getFilePath() {

		return filePath;
	}
}
