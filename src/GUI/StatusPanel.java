package GUI;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;

public class StatusPanel extends JPanel{
	static JLabel lb1,lb2,lb3,lb4,lb5,lb6,lb7,lb8;
    static JTextField tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8;
    static JPanel jp1,jp2,jp3,jp4,jp5;

    public StatusPanel(int size){

        lb1 = new JLabel("Dice Roll:");
        lb1.setFont(new Font("Verdana", Font.PLAIN, size));
        lb2 = new JLabel("Red Pieces in play:");
        lb2.setFont(new Font("Verdana", Font.PLAIN, size));
        lb3 = new JLabel("Red Pieces out of play:");
        lb3.setFont(new Font("Verdana", Font.PLAIN, size));
        lb4 = new JLabel("Red Pieces slain:");
        lb4.setFont(new Font("Verdana", Font.PLAIN, size));
        lb5 = new JLabel("White Pieces in play:");
        lb5.setFont(new Font("Verdana", Font.PLAIN, size));
        lb6 = new JLabel("White Pieces out of play:");
        lb6.setFont(new Font("Verdana", Font.PLAIN, size));
        lb7 = new JLabel("White Pieces slain:");
        lb7.setFont(new Font("Verdana", Font.PLAIN, size));
        lb8 = new JLabel("Current Player");
        lb8.setFont(new Font("Verdana", Font.PLAIN, size));

        tf1=new JTextField(); tf2=new JTextField(); tf3=new JTextField();
        tf4=new JTextField(); tf5=new JTextField(); tf6=new JTextField();
        tf7=new JTextField(); tf8=new JTextField();

        tf1.setEditable(false); tf2.setEditable(false);tf3.setEditable(false);
        tf4.setEditable(false); tf5.setEditable(false);tf6.setEditable(false);
        tf7.setEditable(false); tf8.setEditable(false);


       	this.setLayout(new GridLayout(8,2));
       	this.add(lb1);
       	this.add(tf1);
       	this.add(lb2);
       	this.add(tf2);
       	this.add(lb3);
       	this.add(tf3);
       	this.add(lb4);
       	this.add(tf4);
       	this.add(lb5);
       	this.add(tf5);
       	this.add(lb6);
       	this.add(tf6);
       	this.add(lb7);
       	this.add(tf7);
       	this.add(lb8);
       	this.add(tf8);
    }

    public static void updateDiceRolls(int[] rolls) {
        tf1.setText(Arrays.toString(rolls));
    }

    public static void updateRedPiecesInPlay(int whitePieceInPlay) {
    	tf2.setText(" "+whitePieceInPlay+" ");
    }

    public static void updateRedPiecesOutOfPlay(int whitePieceOutOfPlay) {
    	tf3.setText(" "+whitePieceOutOfPlay+" ");
    }

    public static void updateRedPiecesDied(int piecesDiedForWhite) {
    	tf4.setText(" "+piecesDiedForWhite+" ");
    }

    public static void updateWhitePiecesInPlay(int blackPieceInPlay) {
    	tf5.setText(" "+blackPieceInPlay+" " );
    }

    public static void updateWhitePiecesOutOfPlay(int blackPieceOutOfPlay) {
    	tf6.setText(" "+blackPieceOutOfPlay+" ");
    }

    public static void updateWhitePiecesDied(int piecesDiedForBlack) {
    	tf7.setText(" "+piecesDiedForBlack+" ");
    }

    public static void updateCurrentPlayer(String currentPlayer) {
    	tf8.setText(currentPlayer);
    }
 } 
