package src.khai;


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
            System.out.println(game.allPoints.size());

            boolean isRed = true;
            for (int i = 0; i < game.allPoints.size(); i++) {
                int currentStartX, currentStartY;
                Point p = game.allPoints.get(i);
                if (isRed) {
                    if (i == 12) {
                        g.setColor(Color.yellow);
                    } else {
                        isRed = false;
                        g.setColor(Color.red);
                    }
                } else {
                    isRed = true;
                    g.setColor(Color.yellow);
                }
                if (i < 12) {
                    currentStartX = startX + i * (width + space);
                    currentStartY = startY;
//                    g.fillRect(startX + i * (width + space), startY, width, height);
                    g.fillPolygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);
                } else {
                    currentStartX = startX + (i % 12) * (width + space);
                    currentStartY = startY + height + space;
//                    g.fillRect(startX + (i % 12) * (width + space), startY + height + space, width, height);
                    g.fillPolygon(new int[]{currentStartX, currentStartX + width / 2, currentStartX + width}, new int[]{currentStartY + height, currentStartY, currentStartY + height}, 3);
                }
                if (p.getSize() > 0) {
                    int currentXPiece = currentStartX;
                    for (int k = 0; k < p.getSize(); k++) {
                        if (p.dominantId == 1) {
                            g.setColor(Color.black);
                            g.fillOval(currentXPiece, currentStartY + k * 30, 60, 30);

                        } else {
                            g.setColor(Color.white);
                            g.fillOval(currentXPiece , currentStartY  +k * 30, 60, 30);
                        }
                    }
                }
            }
        }
    }
}
