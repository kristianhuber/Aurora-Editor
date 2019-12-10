package aurora.editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import aurora.util.TextureManager;

public class MenuComponent extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private HashMap<String, SubMenu> menuBarOptions = new HashMap<String, SubMenu>();
	private HashMap<String, MenuOption> options = new HashMap<String, MenuOption>();

	/* Constructor Method */
	public MenuComponent() {

		// UIManager Stuff
		UIManager.put("MenuItem.selectionForeground", Editor.foreground);
		UIManager.put("MenuItem.selectionBackground", Editor.selected);
		UIManager.put("Menu.selectionForeground", Editor.foreground);
		UIManager.put("Menu.selectionBackground", Editor.selected);

		// Settings
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setForeground(Editor.foreground);
		this.setBackground(Editor.component);
		this.setFont(Editor.editFont);

		// All the MenuBar Options
		this.addMenuBarItem("Project");

		// Project and Roots
		this.addMenuOption("Project", "New Project");
		options.get("New Project").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				IOHelper.newProject();
			}
		});

		this.addMenuOption("Project", "Open Project");
		options.get("Open Project").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					IOHelper.openProject(fc.getSelectedFile().getAbsolutePath());
				} else {
					Editor.console.writeToConsole("Project was Not Loaded.",
							"red");
					return;
				}
			}
		});

		this.addMenuOption("Project", "Save Project");
		options.get("Save Project").addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				IOHelper.saveProject();
			}
		});

		this.addMenuOption("Project", "Exit");
		options.get("Exit").addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				System.exit(0);
			}
		});

		// File and Roots
		this.addMenuBarItem("File");
		this.addSubMenu("File", "New Resource");
		this.addSubMenu("File", "New World Component");
		this.addSubMenu("File", "New Code");
		this.addMenuOption("New Resource", "New Texture");
		this.addMenuOption("New Resource", "New Model");
		this.addMenuOption("New World Component", "New Entity");
		this.addMenuOption("New World Component", "New Particle Effect");
		this.addMenuOption("New Code", "New Text File");
		this.addMenuOption("New Code", "New AI File");

		options.get("New Texture").addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				JFileChooser fc = new JFileChooser();

				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

					String name = fc
							.getSelectedFile()
							.getName()
							.substring(0,
									fc.getSelectedFile().getName().length() - 4);

					TextureManager.registerTexture(fc.getSelectedFile()
							.getAbsolutePath(), name);

					Editor.browser.addNode("textures", name + ".png");
				} else {

					Editor.writeError("Texture Was Canceled by User.");
				}
			}
		});
		
		//View and Roots
		this.addMenuBarItem("World");
		this.addMenuOption("World", "Place Object");

		// Run and Roots
		this.addMenuBarItem("Run");
		this.addMenuOption("Run", "Run");

		// Settings and Roots
		this.addMenuBarItem("Settings");
		this.addMenuOption("Settings", "Clear Console");
		options.get("Clear Console").addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				Editor.console.clearConsole();
			}
		});
	}

	/* Adds a MenuBar Option */
	public void addMenuBarItem(String text) {

		SubMenu m = new SubMenu(text, true);

		this.menuBarOptions.put(text, m);
		this.add(m);
	}

	public void addSubMenu(String parent, String text) {

		SubMenu sm = this.menuBarOptions.get(parent);
		SubMenu m = new SubMenu(text, false);

		if ((sm.getItemCount()) % 2 == 0) {

			m.setBackground(Editor.background);
		}

		this.menuBarOptions.put(text, m);
		sm.add(m);
	}

	/* Adds a Menu Option */
	public void addMenuOption(String parent, String text) {

		SubMenu sm = this.menuBarOptions.get(parent);
		MenuOption m = new MenuOption(text);

		if ((sm.getItemCount()) % 2 == 0) {

			m.setBackground(Editor.background);
		}

		options.put(text, m);
		sm.add(m);
	}

	/* SubMenu Class */
	private class SubMenu extends JMenu {
		private static final long serialVersionUID = 1L;

		public SubMenu(String text, boolean dropdown) {

			if(dropdown){
				this.setPreferredSize(new Dimension(60, 20));	
			}else{
				this.setPreferredSize(new Dimension(150, 20));
			}
			
			this.setBorder(BorderFactory.createLineBorder(Editor.selected));
			this.setForeground(Editor.foreground);
			this.setBackground(Editor.component);
			this.setFont(Editor.editFont);
			this.setOpaque(true);
			this.setText(text);
		}
	}

	/* MenuOption Class */
	private class MenuOption extends JMenuItem {
		private static final long serialVersionUID = 1L;

		public MenuOption(String text) {

			this.setPreferredSize(new Dimension(150, 20));
			this.setForeground(Editor.foreground);
			this.setBackground(Editor.component);
			this.setFont(Editor.editFont);
			this.setText(text);
		}
	}
}
