import javax.swing.*;


import GUI.MainMenu;
import Utils.Variables;
import Visualisation.BoardSpace;
import Visualisation.BoardView;
import World.Board;

public class Main {

    public static void main(String[] args) {
        int frameWid=(int)Variables.SCREEN_WIDTH*4/5;
        int frameHei=(int) Variables.SCREEN_HEIGHT*4/5;
        JFrame frame = new JFrame();
        JPanel MainGui = new MainMenu(frame);
        Board b= new Board();

        JPanel boardvis= new BoardView(b,frameWid, frameHei);

        frame.add(boardvis);
        frame.setTitle("Play it Backgammon");
        frame.setSize(frameWid,frameHei);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        //TODO: change the draw class to match the "eaten" pieces in board
        //TODO: make sure we can draw the game and the menu after each other (rn they are both JFrames)
        //TODO: TALK DAMMIT

//
//        b.playerMove(b.getSpaces()[1], b.getSpaces()[2]);
//        b.playerMove(b.getSpaces()[2], b.getSpaces()[6]);
//
//        b.playerMove(b.getSpaces()[24], b.getSpaces()[19]);



        System.out.println(b);







    }

}