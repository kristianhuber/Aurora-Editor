package aurora.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 * @Author: Kristian
 * @Description: Tab Component
 * 
 */

public class OutputComponent extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	private HashMap<String, JPanel> panes = new HashMap<String, JPanel>();

	/* Constructor Method */
	public OutputComponent() {

		this.setUI(new CustomTabs());
	}

	/* Adds a Tab */
	public void addOutputTab(String name) {

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createLineBorder(Editor.selected));
		p.setBackground(Editor.component);
		p.setLayout(new BorderLayout());

		this.panes.put(name, p);
		this.addTab(name, p);
	}

	/* Returns the Tab's JPanel */
	public JPanel getJPanel(String tab) {

		return panes.get(tab);
	}

	/* Not My Class, However I Rewrote It (Mostly) */
	public static class CustomTabs extends BasicTabbedPaneUI {

		private int inclTab = 4;
		private int anchoFocoH = 4;
		private Polygon shape;

		/* Returns an Instance of this */
		public static ComponentUI createUI(JComponent c) {

			return new CustomTabs();
		}

		/* Creates the Background */
		protected void paintTabBackground(Graphics g, int place, int index,
				int x, int y, int w, int h, boolean selected) {

			Graphics2D g2D = (Graphics2D) g;
			GradientPaint gradientShadow;

			// Makes the Polygon
			int[] xp = new int[] { x, x, x + 3, x + w - inclTab - 6,
					x + w - inclTab - 2, x + w - inclTab, x + w - inclTab, x };

			int[] yp = new int[] { y + h, y + 3, y, y, y + 1, y + 3, y + h,
					y + h };

			gradientShadow = new GradientPaint(0, 0, Editor.foreground, 0, y
					+ h / 2, Editor.component);

			shape = new Polygon(xp, yp, xp.length);

			// Paints the Shadow
			g2D.setColor(Editor.foreground);
			g2D.setPaint(gradientShadow);
			g2D.fill(shape);

			if (runCount > 1) {
				g2D.fill(shape);
			}

			g2D.fill(shape);
		}

		/* Paints the Text on the Tabs */
		protected void paintText(Graphics g, int placement, Font font,
				FontMetrics metrics, int index, String text, Rectangle textR,
				boolean selected) {

			// ?
			super.paintText(g, placement, Editor.editFont, metrics, index,
					text, textR, selected);

			g.setFont(Editor.editFont);

			View v = getTextViewForTab(index);

			if (v != null) {

				v.paint(g, textR);
			} else {

				int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(index);

				g.setColor(Editor.foreground);
				BasicGraphicsUtils.drawStringUnderlineCharAt(g, text,
						mnemIndex, textR.x, textR.y + metrics.getAscent());
			}
		}

		/* Calculates Width */
		protected int calculateTabWidth(int tabPlacement, int tabIndex,
				FontMetrics metrics) {
			return 20 + inclTab
					+ super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		}

		/* Calculates Height */
		protected int calculateTabHeight(int tabPlacement, int tabIndex,
				int fontHeight) {

			if (tabPlacement == LEFT || tabPlacement == RIGHT) {
				return super.calculateTabHeight(tabPlacement, tabIndex,
						fontHeight);
			} else {
				return anchoFocoH
						+ super.calculateTabHeight(tabPlacement, tabIndex,
								fontHeight);
			}
		}

		/* Removes the Dashed Border */
		protected void paintTabBorder(Graphics g, int tabPlacement,
				int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		}

		/* Paints the Shadow of the Highlighted Tab */
		protected void paintFocusIndicator(Graphics g, int tabPlace,
				Rectangle[] rectangles, int index, Rectangle iconRectangle,
				Rectangle text, boolean selected) {

			if (tabPane.hasFocus() && selected) {

				g.setColor(UIManager.getColor(Editor.foreground));
				g.drawPolygon(shape);
			}
		}
	}
}
