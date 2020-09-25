package GUI;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class StatusPanel {
   	static JLabel lb1,lb2,lb3,lb4,lb5,lb6,lb7,lb8;
    static JTextField tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8;
    static JPanel jp1,jp2,jp3,jp4,jp5;

    StatusPanel(){
        JFrame f= new JFrame("Player Status");
        JFrame.setDefaultLookAndFeelDecorated(true);
        f.getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GRAY));
        lb1 = new JLabel("Dice Roll:");
        lb1.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb2 = new JLabel("White Pieces in play:");
        lb2.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb3 = new JLabel("White Pieces out of play:");
        lb3.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb4 = new JLabel("White Pieces died:");
        lb4.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb5 = new JLabel("Black Pieces in play:");
        lb5.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb6 = new JLabel("Black Pieces out of play:");
        lb6.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb7 = new JLabel("Black Pieces died:");
        lb7.setFont(new Font("Verdana", Font.PLAIN, 18));
        lb8 = new JLabel("Next Move:");
        lb8.setFont(new Font("Verdana", Font.PLAIN, 18));

        tf1=new JTextField(); tf2=new JTextField(); tf3=new JTextField();
        tf4=new JTextField(); tf5=new JTextField(); tf6=new JTextField();
        tf7=new JTextField(); tf8=new JTextField();
        tf1.setEditable(false); tf2.setEditable(false);tf3.setEditable(false);
        tf4.setEditable(false); tf5.setEditable(false);tf6.setEditable(false);
        tf7.setEditable(false); tf8.setEditable(false);


       	JPanel jp = new JPanel(new GridLayout(8,2));
       	jp.add(lb1);
       	jp.add(tf1);
       	jp.add(lb2);
       	jp.add(tf2);
       	jp.add(lb3);
       	jp.add(tf3);
       	jp.add(lb4);
       	jp.add(tf4);
       	jp.add(lb5);
       	jp.add(tf5);
       	jp.add(lb6);
       	jp.add(tf6);
       	jp.add(lb7);
       	jp.add(tf7);
       	jp.add(lb8);
       	jp.add(tf8);
       	f.setSize(new Dimension(500,500));
       	f.add(jp);
        f.setVisible(true);
    }

    public static void updateDiceRolls(int[] rolls) {
        tf1.setText("First diceroll:" + rolls[0] + "\n" + "Second diceroll:"+ rolls[1]);
    }

    public static void updateWhitePiecesInPlay(int whitePieceInPlay) {
    	tf2.setText(whitePieceInPlay+"");
    }

    public static void updateWhitePiecesOutOfPlay(int whitePieceOutOfPlay) {
    	tf3.setText(whitePieceOutOfPlay+"");
    }

    public static void updateWhitePiecesDied(int piecesDiedForWhite) {
    	tf4.setText(piecesDiedForWhite+"");
    }

    public static void updateBlackPiecesInPlay(int blackPieceInPlay) {
    	tf3.setText(blackPieceInPlay+"");
    }

    public static void updateBlackPiecesOutOfPlay(int blackPieceOutOfPlay) {
    	tf4.setText(blackPieceOutOfPlay+"");
    }

    public static void updateBlackPiecesDied(int piecesDiedForBlack) {
    	tf7.setText(piecesDiedForBlack+"");
    }

    public static void updatePlNextMove(String playerToMove) {
    	tf8.setText(playerToMove+"");
    }
 }
