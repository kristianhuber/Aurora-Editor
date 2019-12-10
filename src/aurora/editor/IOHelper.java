package aurora.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import aurora.util.TextureManager;

public class IOHelper {

	public static void newProject() {
		String path, name;

		// Step 1: Name the Project
		name = JOptionPane.showInputDialog(null, "Enter a Project Name",
				"Aurora", JOptionPane.INFORMATION_MESSAGE);

		if (name.equals(null)) {
			Editor.writeError("Project Creation Canceled by User.");
			return;
		}

		// Step 2: Choose the Directory
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Step 3: Write the Files
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			path = fc.getSelectedFile().getAbsolutePath() + "\\" + name;
		} else {
			Editor.writeError("Project Creation Canceled by User.");
			return;
		}

		// Step 4: Make Sure the Files Don't Get Overridden
		if (new File(path).exists()) {
			int reply = JOptionPane.showConfirmDialog(null,
					"Overwrite Project?", "Aurora", JOptionPane.YES_NO_OPTION);

			if (reply == JOptionPane.YES_OPTION) {

				new File(path).delete();
			} else {

				Editor.writeError("Project Creation Canceled by User.");
				return;
			}
		}

		if (!IOHelper.writeProjectDirectory(path)) {
			return;
		}

		// Step 5: Finish
		Editor.console.clearConsole();
		Editor.writeOutput("Created Project " + name);
		Editor.console.writeToConsole(path);

		Editor.browser.loadDefaultNodes(name);
		Editor.setProjectName(name);
		Editor.setFilePath(path);
		Editor.browser.setEnabled(true);
	}

	public static void openProject(String path) {

		try {
			File config = new File(path + "\\config.dat");

			// Checks the IO
			if (!config.exists()) {

				Editor.writeError("Could Not File config.dat.");
				return;
			}

			FileReader fr = new FileReader(config.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);

			// Writes the Variables
			String title = br.readLine().split(":")[1];

			// Writes the Textures
			String[] textures = br.readLine().split(":")[1].split(" ");
			System.out.println(textures);

			// Closes the IO
			br.close();

			Editor.browser.setEnabled(true);
			Editor.browser.loadDefaultNodes(title);

			for (String s : textures) {
				Editor.browser.addNode("Textures", s + ".png");
			}

			Editor.console.writeToConsole("Opened Project.", "green");

		} catch (IOException e) {
			Editor.console.writeToConsole("Could not Open Project.", "red");
		}
	}

	public static void saveProject() {

		try {
			File config = new File(Editor.getFilePath() + "\\config.dat");

			// Ready the IO
			if (!config.exists())
				config.createNewFile();

			FileWriter fw = new FileWriter(config.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Writes the Variables
			bw.write("Title:" + Editor.getProjectName());
			bw.newLine();

			// Writes the Textures
			bw.write("Textures:");
			for (String name : TextureManager.getAllTextureNames()) {

				bw.write(name + " ");
			}
			bw.newLine();

			// Closes the IO
			bw.close();

			Editor.console.writeToConsole("Saved Project.", "green");
		} catch (IOException e) {
			Editor.console.writeToConsole("Could not Save File.", "red");
		}
	}

	private static boolean writeProjectDirectory(String path) {

		try {

			new File(path).mkdir();
			new File(path + "\\textures").mkdir();
			new File(path + "\\models").mkdir();
			new File(path + "\\config.dat").createNewFile();
		} catch (Exception e) {

			Editor.writeError("Could Not Write To Directory.");
			return false;
		}

		return true;
	}
}
