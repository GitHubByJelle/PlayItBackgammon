package Visualisation;


import World.Space;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {
    int startX = 100;
    int startY = 100;
    boolean isRed = true;
    int width = 60;
    int height = 200;
    int space = 15;
    Game game;

    public Draw(Game game) {
        this.game = game;
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
            System.out.println(game.allSpaces.size());

            boolean isBlue = true;
            for (int i = 0; i < game.allSpaces.size(); i++) {
                int currentStartX, currentStartY;
                Space p = game.allSpaces.get(i);
                if (isBlue) {
                    if (i == 13) {
                        g.setColor(Color.white);
                    } else {
                        isBlue = false;
                        g.setColor(new Color(51,204,255));
                    }
                } else {
                    isBlue = true;
                    g.setColor(Color.white);
                }
                if (i < 13) {

                    if (i == 6) {
                        currentStartX = startX +  (13-i) * (width + space);
                        currentStartY = startY+ height+ space;
                    } else {
                        currentStartX = startX +  (13-i) * (width + space) -space;
                        currentStartY = startY+ height+ space;
//                    g.fillRect(startX + i * (width + space), startY, width, height);
                        g.fillPolygon(new int[]{currentStartX, currentStartX - width/2, currentStartX - width }, new int[]{currentStartY+height, currentStartY, currentStartY + height}, 3);
                    }
                }else {
                    if (i == 19) {
                        currentStartX = startX + (i%13) * (width + space);
                        currentStartY = startY;
                    } else {
                        currentStartX = startX + (i % 13) * (width + space);
                        currentStartY = startY;
//                    g.fillRect(startX + (i % 12) * (width + space), startY + height + space, width, height);
                        g.fillPolygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);
                    }
                }
                if (p.getSize() > 0) {
                    if (i<13){
                        int currentXPiece = currentStartX - (width);
                        for (int k = 1; k <= p.getSize(); k++) {
                            if (p.getDominantId() == 1) {
                                g.setColor(Color.black);

                                if(p.getSize()==5)
                                    g.fillOval(currentXPiece, currentStartY+ 20 + k * 30, 60, 30);
                                else
                                    g.fillOval(currentXPiece, currentStartY+ 80 + k * 30, 60, 30);


                            } else {
                                g.setColor(new Color(204, 204, 204));

                                if(p.getSize()==5)
                                    g.fillOval(currentXPiece, currentStartY+ 20 + k * 30, 60, 30);
                                else
                                    g.fillOval(currentXPiece, currentStartY+ 110 + k * 30, 60, 30);
                            }
                        }
                    }
               else{
                   int currentXPiece = currentStartX;
                                      for (int k = 0; k < p.getSize(); k++) {
                                          if (p.getDominantId() == 1) {
                                           g.setColor(Color.black);
                                              g.fillOval(currentXPiece, currentStartY + k * 30, 60, 30);

                                           } else {
                                               g.setColor(new Color(204, 204, 204));
                                              g.fillOval(currentXPiece, currentStartY + k * 30, 60, 30);
                                           }
                                      }
                                 }

                        }

            }
        }
    }
}
