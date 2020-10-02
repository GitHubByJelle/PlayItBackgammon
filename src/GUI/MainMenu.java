package GUI;

import GUI.PlayersChoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel {

    private Image img;
    private JPanel panel;
    public MainMenu(JFrame frame) {
        this.img = new ImageIcon("pics/Bg gui.jpg").getImage();

        this.panel = this;
        setLayout(null);
        // Start button
        JButton start = ButtonCreator("Start");
        start.setBounds(375, 200, 170, 40);
        start.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                PlayersChoice panel2 = new PlayersChoice(frame,panel);
                frame.add(panel2);
            }
        } );
        // quit button
        JButton quit = ButtonCreator("Quit");
        quit.setBounds(375, 300, 170, 40);
        quit.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        } );
        add(start);
        add(quit);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);

    }

    private JButton ButtonCreator(String ButtonName){
        JButton button = new JButton(ButtonName);
        Color buttonColor = new Color(138, 69, 0);
        button.setBackground(buttonColor);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setForeground(Color.BLACK);

        return button;

    }
}
