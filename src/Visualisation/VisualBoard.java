package Visualisation;

import World.Board;
import World.Piece;
import World.Space;
import World.Die;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

public class VisualBoard extends JPanel{

        static TestPane.TriangleShape triangleShape;
        private static Board board= new Board(1000, 700);
        private final JPanel pane = new JPanel(new BorderLayout(3, 3));
        private JPanel c1Board, c2Board;
        private static JLabel mess = new JLabel("Backgammon board");
        JToolBar tool = new JToolBar();
        Insets Margin = new Insets(0, 0, 0, 0);
        Space spaces;


        public static void main(String[] args) {

            JFrame frame= new JFrame();
            frame.setSize(board.getWidth(), board.getHeight());
            frame.getContentPane().add(new VisualBoard());
            frame.setLocationRelativeTo(null);
            frame.setBackground(Color.LIGHT_GRAY);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.add(mess);
            frame.add(new TestPane());

        }

        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(51,204,255));

            g2d.fill(triangleShape);
        }

        static class TestPane extends JPanel {


            public TestPane() {
                triangleShape = new TriangleShape(new Point2D.Double((board.getWidth()/12)/2, board.getHeight()/2),
                        new Point2D.Double(board.getWidth()/12, 0), new Point2D.Double(1, 0));
            }

            class TriangleShape extends Path2D.Double {
                public TriangleShape(Point2D... points) {
                    moveTo(points[0].getX(), points[0].getY());
                    lineTo(points[1].getX(), points[1].getY());
                    lineTo(points[2].getX(), points[2].getY());
                    closePath();
                }
            }
        }
    }

