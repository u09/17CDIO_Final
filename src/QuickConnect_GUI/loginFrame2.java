package QuickConnect_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class loginFrame2 extends JFrame {

	JPanel panel = new JPanel();
	String myFont = "Times New Roman";

	public static void showLoginFrame() {
		loginFrame2 frameTabel = new loginFrame2("show", "show");
	}

	loginFrame2(String username, String password) {
		this.setTitle("LiveChat - Bruger: " + username);
		this.setSize(500, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(null);

		JLabel logWelcome = new JLabel("Velkommen til LiveChat " + username);
		logWelcome.setBounds(120, 0, 300, 60);
		logWelcome.setFont(new Font(myFont, 1, 15));

		panel.add(logWelcome);

		this.add(panel);
		setVisible(true);
	}

	public void changePass() {

	}
	
	public void changeNickname() {
		
	}

}
