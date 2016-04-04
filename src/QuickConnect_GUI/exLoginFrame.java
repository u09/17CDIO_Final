package QuickConnect_GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class exLoginFrame {

	StandardTypes standard = new StandardTypes();
	JPanel panel = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	
	exLoginFrame() {
		
		c.fill = GridBagConstraints.CENTER;
		
		JLabel label;
		JTextField textField;
		JPasswordField passwordField;
		JButton button;
		
		c.weighty = 1;
		label = new JLabel("Velkommen til LiveChat");
		label.setFont(standard.getMyFont(1, 24));
		c.gridy = 0;
		panel.add(label, c);
		
		c.weighty = 0.5;
		label = new JLabel("Indtast brugernavn:");
		label.setFont(standard.getMyFont(0, 18));
		c.gridy = 1;
		panel.add(label, c);
		
		c.weighty = 0;
		textField = new JTextField(15);
		c.gridy = 2;
		panel.add(textField, c);

		c.weighty = 0.5;
		label = new JLabel("Indtast adgangskode:");
		label.setFont(standard.getMyFont(0, 18));
		c.gridy = 3;
		panel.add(label, c);
		
		c.weighty = 0;
		passwordField = new JPasswordField(15);
		c.gridy = 4;
		panel.add(passwordField, c);
		
		c.weighty = 1;
		button = new JButton("Login");
		button.setFont(standard.getMyFont(0, 15));
		c.gridy = 5;
		panel.add(button, c);
		
		c.weighty = 0;
		label = new JLabel("Har du ikke en bruger?");
		label.setFont(standard.getMyFont(0, 16));
		c.gridy = 6;
		panel.add(label, c);
		
		c.weighty = 0.2;
		label = new JLabel("Registrer dig her:");
		label.setFont(standard.getMyFont(0, 16));
		c.gridy = 7;
		panel.add(label, c);
		
		c.weighty = 0;
		button = new JButton("Registrer");
		button.setFont(standard.getMyFont(0, 15));
		c.gridy = 8;
		c.weighty = 1;
		panel.add(button, c);
	}
	
}
