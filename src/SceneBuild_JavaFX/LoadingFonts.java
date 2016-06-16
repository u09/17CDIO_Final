package SceneBuild_JavaFX;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LoadingFonts implements ActionListener {
	@SuppressWarnings("rawtypes")
	JComboBox fontCombo;
	JLabel label;
	Font font;

	@SuppressWarnings({"unchecked","rawtypes"})
	public LoadingFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();
		fontCombo = new JComboBox(fontNames);
		fontCombo.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = (String) fontCombo.getSelectedItem();
		font = Font.decode(name).deriveFont(24f);
		label.setFont(font);
		label.setText(name);
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.add(fontCombo);
		return panel;
	}

	@SuppressWarnings("serial")
	private JLabel getLabel() {
		String name = (String) fontCombo.getItemAt(0);
		font = new Font(name, Font.PLAIN, 24);
		label = new JLabel(name, SwingConstants.CENTER) {
			@Override
			protected void paintComponent(Graphics g) {
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		label.setFont(font);
		return label;
	}

	public static void main(String[] args) {
		LoadingFonts app = new LoadingFonts();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(app.getPanel(), "North");
		f.getContentPane().add(app.getLabel());
		f.setSize(300, 180);
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
