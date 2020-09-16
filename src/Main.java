import javax.swing.*;


import GUI.MainMenu;
import Visualisation.Draw;
import World.Board;
import World.Die;
import World.Space;

public class Main {

    public static void main(String[] args) {

//        JFrame frame = new JFrame();
//        JPanel MainGui = new MainMenu(frame);
//        frame.add(MainGui);
//        frame.setTitle("Play it Backgammon");
//        frame.setSize(900,650);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);


        //TODO: change the draw class to match the "eaten" pieces in board
        //TODO: make sure we can draw the game and the menu after each other (rn they are both JFrames)
        //TODO: TALK DAMMIT
        Board b= new Board();
        Draw d= new Draw(b);
        d.run();

        b.playerMove(b.getSpaces()[1], b.getSpaces()[2]);
        b.playerMove(b.getSpaces()[2], b.getSpaces()[6]);



        System.out.println(b);







    }

}