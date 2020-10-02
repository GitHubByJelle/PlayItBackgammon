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
        int frameWid=(int) Variables.FRAME_WIDTH;
        int frameHei=(int) Variables.FRAME_HEIGHT;
        JFrame frame = new JFrame();
        JPanel MainGui = new MainMenu(frame);

        frame.add(MainGui);

        frame.setTitle("Play it Backgammon");
        frame.setSize(frameWid,frameHei);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



    }

}
