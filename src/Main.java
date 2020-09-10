import javax.swing.*;


import GUI.MainMenu;
import World.Board;

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

        System.out.println(b);



    }

}