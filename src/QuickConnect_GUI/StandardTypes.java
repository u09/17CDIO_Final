package QuickConnect_GUI;

import java.awt.Font;

import javax.swing.*;

public class StandardTypes {

	private Font myFont;
	private JButton myButton;
	
	/**
	 * Sets the font to Times New Roman with the desired style and size
	 * @param style - int
	 * @param size - int
	 * @return
	 */
	public Font getMyFont(int style, int size) {
		myFont = new Font("Times New Roman", style, size);
		return myFont;
	}
	
//	public JLabel getMyJLabel(String text, int x, int y, int size) {
//		
//	}
	
//	public JLabel getMyJLabel1(String text, int x, int y, int size) {
//
//	}
	
//	public JButton getMyJButton() {
//		
//	}
	
}
