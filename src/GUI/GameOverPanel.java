package GUI;

import Utils.Variables;
import World.Player;
import com.sun.tools.javac.Main;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

public class GameOverPanel extends JPanel{

	JTextField tf;
	JButton restart;
	JButton exit;

	public GameOverPanel(JFrame frame){
		this.setLayout(new GridLayout(3,1));
		Color buttonColor = new Color(138, 69, 0);
		tf = new JTextField();
		tf.setEditable(false);
		tf.setBackground(buttonColor);
		tf.setBorder(new LineBorder(Color.BLACK));
		tf.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		tf.setForeground(Color.BLACK);
		

		restart = new JButton("Play again");
		exit = new JButton("Exit");
        restart.setBackground(buttonColor);
        restart.setBorder(new LineBorder(Color.BLACK));
		restart.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		restart.setForeground(Color.BLACK);
		
		exit.setBackground(buttonColor);
        exit.setBorder(new LineBorder(Color.BLACK));
        exit.setFont(new Font("Segoe UI", Font.PLAIN, 50));
		exit.setForeground(Color.BLACK);

		exit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		restart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
				int frameWid=(int) Variables.FRAME_WIDTH;
				int frameHei=(int) Variables.FRAME_HEIGHT;
				JPanel MainGui = new MainMenu(frame);
				frame.add(MainGui);

				frame.setTitle("Play it Backgammon");
				frame.setSize(frameWid,frameHei);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
			}
		});
		this.add(tf);
		this.add(restart);
		this.add(exit);
	}

	public void updateResult(String player) {
		tf.setText(player + " won");
		tf.setHorizontalAlignment(JLabel.CENTER);
	}
}
