package Livechat_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class registerFrame extends JFrame {

	public static void showRegisterFrame() {
		registerFrame frameTabel = new registerFrame();
	}
	
	JPanel panel = new JPanel();
	String myFont = "Iowan Old Style";
	JLabel welcome = new JLabel("Registrering til LiveChat");
	JLabel luser = new JLabel("Indtast dit ønskede brugernavn:");
	JTextField username = new JTextField("Brugernavn", 15);
	JLabel lpass = new JLabel("Indtast din ønskede kode:");
	JPasswordField pass = new JPasswordField("Password", 15);
	JLabel lpass1 = new JLabel("Gentag din kode:");
	JPasswordField pass1 = new JPasswordField("Password", 15);
	JButton bconfirm = new JButton("Godkend");
	Point myPoint = new Point(650, 280);
	JButton bback = new JButton("Tilbage");

	registerFrame() {
		this.setTitle("LiveChat - Registrering");
		this.setSize(300, 330);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel.setLayout(null);
		panel.setName("LiveChat - Registrering");

		welcome.setBounds(35, 5, 300, 50);
		luser.setBounds(45, 45, 300, 50);
		username.setBounds(45, 90, 215, 20);
		lpass.setBounds(65, 105, 300, 50);
		pass.setBounds(45, 150, 215, 20);
		lpass1.setBounds(90, 160, 300, 60);
		pass1.setBounds(45, 210, 215, 20);
		bconfirm.setBounds(155, 250, 100, 40);
		bback.setBounds(45,250,100,40);

		welcome.setFont(new Font(myFont, 1, 20));
		luser.setFont(new Font(myFont, 0, 15));
		lpass.setFont(new Font(myFont, 0, 15));
		lpass1.setFont(new Font(myFont, 0, 15));
		bconfirm.setFont(new Font(myFont, 0, 14));
		bback.setFont(new Font(myFont, 0, 14));

		panel.add(welcome);
		panel.add(luser);
		panel.add(lpass);
		panel.add(lpass1);
		panel.add(username);
		panel.add(pass);
		panel.add(pass1);
		panel.add(bconfirm);
		panel.add(bback);

		this.add(panel);
		getRootPane().setDefaultButton(bconfirm);
		setVisible(true);
		actionConfirm();
		actionBack();
	}

	public void actionConfirm() {
		bconfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String cuser = username.getText();
				String cpass1 = pass.getText();
				String cpass2 = pass1.getText();
				
				boolean check = checkRegister(cuser, cpass1, cpass2);
				if(check) {
					addUser(cuser, cpass1);
					JOptionPane.showMessageDialog(panel,
		                    "<html>Du er registreret!<br><br>Tryk OK for at gå til login siden", panel.getName(),
		                    JOptionPane.INFORMATION_MESSAGE);
						dispose();
				}
				else {
					JOptionPane.showMessageDialog(panel,
		                    "<html>Registrering mislykkedes!<br><br>Begge password skal være ens", panel.getName(),
		                    JOptionPane.WARNING_MESSAGE);
					pass.setText("");
					pass1.setText("");
					pass.requestFocusInWindow();
				}
			}
		});
	}
	
	public void addUser(String user, String pass) {

	}

	public boolean checkRegister(String user, String pass1, String pass2) {
		if(pass1.equals(pass2)) return true;
		else return false;
	}
	
	public void actionBack() {
		bback.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Start start = new Start();
				dispose();
				start.setVisible(true);
			}
		});
	}
}
