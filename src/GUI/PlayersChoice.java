package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Utils.Variables;
import Visualisation.BoardView;
import World.Board;
import World.GameLoop;

public class PlayersChoice extends JPanel {

    private Image img;
    private JComboBox player1;
    private JComboBox player2;
    private JFrame frame;
    public PlayersChoice(JFrame frame, JPanel main) {
        this.frame=frame;
        this.img = new ImageIcon("pics/Player choice.jpg").getImage();
        setLayout(null);
        Variables var = new Variables();
        String[] players = (var.PLAYERS);
        player1 = new JComboBox(players);
        player1.setBackground(new Color(138, 69, 0));
        player1.setForeground(Color.WHITE);
        player1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        player1.setBounds(130, 300, 170, 40);

        player2 = new JComboBox(players);
        player2.setBackground(new Color(138, 69, 0));
        player2.setForeground(Color.WHITE);
        player2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        player2.setBounds(560, 300, 170, 40);

        // Play button
        JButton play = ButtonCreator("Play");
        play.setBounds(350, 450, 170, 40);
        play.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String choice1 = (String) player1.getSelectedItem();
                String choice2 = (String) player2.getSelectedItem();
                System.out.println(choice1 + " vs " + choice2);
                gameSetup(choice1,choice2);
            }
        } );
        // Back button
        JButton back = ButtonCreator("Back");
        back.setBounds(350, 500, 170, 40);
        back.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            setVisible(false);
            frame.add(main);
            main.setVisible(true);
            }
        } );
        add(player1);
        add(player2);
        add(play);
        add(back);

    }

    private void gameSetup(String choice1, String choice2) {
        setVisible(false);
        Board b= new Board();
        b.setPlayers("Human","Human");
        b.createLoop();
        System.out.println(b);
        JPanel boardvis= new BoardView(b, frame.getWidth(), frame.getHeight());

        ((BoardView) boardvis).addStatPane(frame);
        frame.setSize((int)Variables.FRAME_WIDTH+250,(int)Variables.FRAME_HEIGHT);
        frame.add(boardvis);
       // g.start();

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
        button.setForeground(Color.WHITE);

        return button;

    }
}
