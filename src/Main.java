import javax.swing.*;


import GUI.MainMenu;
import World.Board;
import World.Die;
import World.Space;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        JPanel MainGui = new MainMenu(frame);
        frame.add(MainGui);
        frame.setTitle("Play it Backgammon");
        frame.setSize(900,650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



        Board b= new Board();
        Space[] s = b.getSpaces();
        System.out.println(b);

        b.playerMove(s[6],s[2]);
        System.out.println(b);






    }

}