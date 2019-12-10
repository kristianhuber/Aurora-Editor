package aurora.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FileBrowserComponent extends JTree {
	private static final long serialVersionUID = 1L;

	private HashMap<String, DefaultMutableTreeNode> actualNodes = new HashMap<String, DefaultMutableTreeNode>();
	private HashMap<String, ArrayList<String>> nodes = new HashMap<String, ArrayList<String>>();

	/* Constructor Method */
	public FileBrowserComponent() {

		// UIManager Stuff
		UIManager.getDefaults().put("TabbedPane.contentBorderInsets",
				new Insets(0, 0, 0, 0));
		UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);

		// Sets the Properties
		this.putClientProperty("JTree.lineStyle", "Horizontal");
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setCellRenderer(new CustomCellRenderer());
		this.loadDefaultNodes("New Project");
		this.setBackground(Editor.component);
		this.setFont(Editor.editFont);
		this.setEnabled(false);

		this.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent tse) {

				JTree t = (JTree) tse.getSource();
				String comp = t.getLastSelectedPathComponent().toString();
				Editor.write(comp);
			}
		});
	}

	/* Loads the Default Nodes */
	public void loadDefaultNodes(String name) {

		// Resets the Lists
		actualNodes.clear();
		nodes.clear();

		// First Node
		actualNodes.put(name, new DefaultMutableTreeNode(name));
		nodes.put(name, new ArrayList<String>());

		// Following Nodes
		this.addNode(name, "Resources");
		this.addNode("Resources", "Textures");
		this.addNode("Resources", "Models");

		this.addNode(name, "World");
		this.addNode("World", "Entities");
		this.addNode("World", "Particles");

		this.addNode(name, "Code");
		this.addNode("Code", "AI Files");

		// Displays the Nodes
		this.setModel(new DefaultTreeModel(actualNodes.get(name)));

		this.expandPath(new TreePath(actualNodes.get("Resources").getPath()));
		this.expandPath(new TreePath(actualNodes.get("World").getPath()));
		this.expandPath(new TreePath(actualNodes.get("Code").getPath()));
	}

	/* Adds a Node to the Tree */
	public void addNode(String parent, String name) {

		actualNodes.put(name, new DefaultMutableTreeNode(name));
		actualNodes.get(parent).add(actualNodes.get(name));
		nodes.put(name, new ArrayList<String>());
		nodes.get(parent).add(name);
	}

	/* Renders the Tree Leaves Custom Colors */
	public class CustomCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Color getBackgroundNonSelectionColor() {
			return null;
		}

		@Override
		public Color getBackgroundSelectionColor() {
			return Editor.selected;
		}

		@Override
		public Color getBackground() {
			return null;
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			Component ret = super.getTreeCellRendererComponent(tree, value,
					selected, expanded, leaf, row, hasFocus);

			@SuppressWarnings("unused")
			DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
			this.setForeground(Editor.foreground);
			this.setText(value.toString());

			return ret;
		}
	}
}
