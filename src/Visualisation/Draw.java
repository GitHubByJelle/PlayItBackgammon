package Visualisation;


import Utils.Variables;
import World.Board;
import World.Space;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {
    int startX = 100;
    int startY = 100;
    int width = 80;
    int height = 200;
    int space = 15;
    Board board;

    public Draw(Board b) {
        board=b;
    }

    public void run() {
        this.setSize(1280, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        MiniDraw miniDraw = new MiniDraw();
        this.getContentPane().add(miniDraw);
        this.setVisible(true);
    }



    class MiniDraw extends JPanel {
        @Override
        protected void paintComponent(Graphics g1) {
            Graphics2D g = (Graphics2D) g1;
            g.setColor(Color.BLUE);
            //System.out.println(board.getSpaces);

            for (int i = 0; i < board.getSpaces().length; i++) {
                //SPACE PLACEMENTS
                int currentStartX, currentStartY;
                Space p = board.getSpaces()[i];

                if(i%2==0){
                    g.setColor(Variables.EVEN_SPACES_COLOR);
                }else{
                    g.setColor(Variables.ODD_SPACES_COLOR);
                }

                if (i < 13) {
                    if (i == 0) {
                        currentStartX = startX +  (13-i) * (width + space);
                        currentStartY = startY+ height+ space;
                    } else {
                        currentStartX = startX +  (13-i) * (width + space) -space;
                        currentStartY = startY+ height+ space;
                        g.fillPolygon(new int[]{currentStartX, currentStartX - width/2, currentStartX - width }, new int[]{currentStartY+height, currentStartY, currentStartY + height}, 3);
                    }

                }else {
                    if (i == 25) {
                        currentStartX = startX + (i%13) * (width + space);
                        currentStartY = startY;
                    } else {
                        currentStartX = startX + (i % 13) * (width + space);
                        currentStartY = startY;
                        g.fillPolygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);
                    }
                }

                //PIECE PLACEMENTS
                if (p.getSize() > 0) {
                    if (i<13){
                        int currentXPiece = currentStartX - (width);
                        for (int k = 1; k <= p.getSize(); k++) {
                            g.setColor(p.getPieces().get(k-1).getColor());
                           g.fillOval(currentXPiece, currentStartY+height- k*30, width, 30);
                        }
                    }else{
                        int currentXPiece = currentStartX;
                        for (int k = 0; k < p.getSize(); k++) {
                            g.setColor(p.getPieces().get(k).getColor());
                            g.fillOval(currentXPiece, currentStartY + k * 30, width, 30);
                        }
                   }
                }
            }
        }
    }
}
