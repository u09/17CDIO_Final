package Livechat_GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
  
public class LoadingFonts implements ActionListener
{
    JComboBox fontCombo;
    JLabel label;
    Font font;
  
    public LoadingFonts()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontCombo = new JComboBox(fontNames);
        fontCombo.addActionListener(this);
    }
  
    public void actionPerformed(ActionEvent e)
    {
        String name = (String)fontCombo.getSelectedItem();
        font = Font.decode(name).deriveFont(24f);
        label.setFont(font);
        label.setText(name);
    }
  
    private JPanel getPanel()
    {
        JPanel panel = new JPanel();
        panel.add(fontCombo);
        return panel;
    }
  
    private JLabel getLabel()
    {
        String name = (String)fontCombo.getItemAt(0);
        font = new Font(name, Font.PLAIN, 24);
        label = new JLabel(name, JLabel.CENTER)
        {
            protected void paintComponent(Graphics g)
            {
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                 RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        label.setFont(font);
        return label;
    }
  
    public static void main(String[] args)
    {
        LoadingFonts app = new LoadingFonts();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(app.getPanel(), "North");
        f.getContentPane().add(app.getLabel());
        f.setSize(300,180);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}