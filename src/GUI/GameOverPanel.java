package GUI;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOverPanel extends JPanel{

	JTextField tf;
	JButton restart;
	JButton exit;

	GameOverPanel(){
		this.setLayout(new GridLayout(3,1));
		tf = new JTextField();
		tf.setEditable(false);
		restart = new JButton("Play again");
		exit = new JButton("Exit");
		exit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		restart.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//
			}
		});
		this.add(tf);
		this.add(restart);
		this.add(exit);
	}

	public void updateResult(String player) {
		tf.setText("player " + player + "won");
	}
}
