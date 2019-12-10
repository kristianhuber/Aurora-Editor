package aurora.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * @Author: Kristian
 * @Description: Console Component
 * 
 */

public class ConsoleComponent extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> output = new DefaultListModel<String>();
	private JList<String> console = new JList<String>();

	private Font f = new Font("Consolas", Font.BOLD, 12);
	
	/* Constructor */
	public ConsoleComponent() {

		console.setCellRenderer(new ZebraCells());
		console.setModel(output);
		console.setBorder(null);
		console.setFont(f);

		this.setBorder(BorderFactory.createEmptyBorder());
		this.setViewportView(console);

		for (int i = 0; i < 15; i++) {

			output.addElement(" ");
		}

		this.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			Dimension none = new Dimension(0, 0);
			
			@Override
			protected JButton createDecreaseButton(int orientation) {
				JButton j = new JButton();
				j.setPreferredSize(none);
				j.setMinimumSize(none);
				j.setMaximumSize(none);
				
				return j;
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				JButton j = new JButton();
				j.setPreferredSize(none);
				j.setMinimumSize(none);
				j.setMaximumSize(none);
				
				return j;
			}

			@Override
			protected void configureScrollBarColors() {
				
				this.thumbColor = Editor.foreground;
				this.trackColor = Editor.selected;
			}
		});
	}

	/* Renders the Cells Different Colors */
	private static class ZebraCells extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			Component cell = super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			
			if (index % 2 == 0) {
				
				cell.setBackground(Editor.background);
			} else {
				
				cell.setBackground(Editor.component);
			}
			
			return cell;
		}
	}

	/* Writes a Message to the Console With Color */
	public void writeToConsole(String s, String color) {

		output.add(0, "<html><font color='" + color + "'>" + s
				+ "</font></html>");
	}

	/* Writes a Plain Message to the Console */
	public void writeToConsole(String s) {

		writeToConsole(s, "orange");
	}

	/* Clears the Console */
	public void clearConsole() {

		output.clear();

		for (int i = 0; i < 15; i++) {

			output.addElement(" ");
		}
	}
}
