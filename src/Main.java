import GUI.MainMenu;
import Utils.Variables;
import Visualisation.BoardView;
import World.Board;
import World.Space;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int frameWid=(int) Variables.SCREEN_WIDTH*4/5;
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


        System.out.println(b);

        b.playerMove(24,23);
        b.playerMove(1,2);

        System.out.println(b);

        System.out.println(Arrays.toString(b.getDie().getCurRoll()));



    }

}