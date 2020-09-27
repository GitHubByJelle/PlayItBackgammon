import GUI.MainMenu;
import GUI.StatusPanel;
import Utils.Variables;
import Visualisation.BoardView;
import World.Board;
import World.GameLoop;
import World.Space;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int frameWid=(int) Variables.SCREEN_WIDTH*4/5;
        int frameHei=(int) Variables.SCREEN_HEIGHT*4/5;
        JFrame frame = new JFrame();
        JPanel MainGui = new MainMenu(frame);
      //  Board b= new Board();
       // b.setPlayers("Human","Human");
        //new StatusPanel();

      //  JPanel boardvis= new BoardView(b,frameWid, frameHei);
        //GameLoop g= new GameLoop(b);
        //((BoardView) boardvis).addStatPane(frame);
        //frame.add(boardvis);

        frame.add(MainGui);

        frame.setTitle("Play it Backgammon");
        frame.setSize(frameWid,frameHei);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);







    }

}
