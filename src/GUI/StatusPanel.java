package GUI;

import Utils.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class StatusPanel extends JPanel{
	static JLabel[] lb= new JLabel[8];
    static JTextField[] tf= new JTextField[8];

    public StatusPanel(int size){

        lb[0] = new JLabel("Dice Roll:");
        lb[0].setFont(new Font("Verdana", Font.PLAIN, size));

        lb[1] = new JLabel("Red Pieces in play:");
        lb[1].setFont(new Font("Verdana", Font.PLAIN, size));
        lb[2] = new JLabel("Red Pieces out of play:");
        lb[2].setFont(new Font("Verdana", Font.PLAIN, size));
        lb[3] = new JLabel("Red Pieces slain:");
        lb[3].setFont(new Font("Verdana", Font.PLAIN, size));

        lb[4] = new JLabel("White Pieces in play:");
        lb[4].setFont(new Font("Verdana", Font.PLAIN, size));
        lb[5] = new JLabel("White Pieces out of play:");
        lb[5].setFont(new Font("Verdana", Font.PLAIN, size));
        lb[6] = new JLabel("White Pieces slain:");
        lb[6].setFont(new Font("Verdana", Font.PLAIN, size));

        lb[7] = new JLabel("Current Player:");
        lb[7].setFont(new Font("Verdana", Font.PLAIN, size));

        this.setLayout(new GridLayout(8,2));
        for(int i=0;i<8;i++){
            tf[i]=new JTextField();
            tf[i].setEditable(false);
            this.add(lb[i]);
            this.add(tf[i]);
        }



    }



    public static void updateDiceRolls(int[] rolls) {
        tf[0].setText(Arrays.toString(rolls));
    }

    public static void updateRedPiecesInPlay(int n) {
    	tf[1].setText(" "+n+" ");
    }

    public static void updateRedPiecesOutOfPlay(int n) {
    	tf[2].setText(" "+n+" ");
    }

    public static void updateRedPiecesDied(int n) {
    	tf[3].setText(" "+n+" ");
    }

    public static void updateWhitePiecesInPlay(int n) {
    	tf[4].setText(" "+n+" " );
    }

    public static void updateWhitePiecesOutOfPlay(int n) {
    	tf[5].setText(" "+n+" ");
    }

    public static void updateWhitePiecesDied(int n) {
    	tf[6].setText(" "+n+" ");
    }

    public static void updateCurrentPlayer(String currentPlayer) {
    	tf[7].setText(currentPlayer);
    }
 } 
