package Visualisation;

import Utils.Variables;
import World.Board;


import World.Space;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class InputHandler implements MouseListener {
    Shape[] visSpaces;
    ArrayList<Ellipse2D> visPieces;
    boolean selected = false;
    Board board;
    int spaceRecord;
    BoardView bv;
    Space s;
    boolean canMove = false;
    int currentPlayer = 0;

    public InputHandler(int startPlayer, Shape[] s, ArrayList<Ellipse2D> e, BoardView bv) {
        visPieces = e;
        visSpaces = s;
        board = bv.board;
        this.bv = bv;
        currentPlayer = startPlayer;
        System.out.println("Current player is: " + currentPlayer);

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {


        if (!selected) {
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    System.out.println(mouseEvent.getX() + " " + mouseEvent.getY() + "DIT ME MAY");
                    selected = true;
                    for (int n = 1; n < visSpaces.length; n++) {
                        if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                            spaceRecord = n;
                            // System.out.println("space number "+ n);

                            int currentStartX, currentStartY;
                            Polygon coloredSpace;
                            Graphics g = bv.getGraphics();
                            g.setColor(Variables.RECOLOR_SPACES_COLOR);


                            ArrayList<Space> arr = board.getValidMoves(board.getSpaces()[n]);
                            for (int j = 0; j < arr.size(); j++) {
                                int id= arr.get(j).getId();
                                int space= bv.getSpace();
                                int spaceWidth= bv.getWidth()/18;
                                int spaceHeight= bv.getHeight()/3;


                                if (id < 13) {
                                    currentStartX = bv.getStartX() + (13 - id) *(bv.getWidth()/20 + (space-1)) -(space);
                                    currentStartY = bv.getStartY() + spaceHeight + space;
                                    g.fillRect(currentStartX,currentStartY,spaceWidth, spaceHeight+space);

                                } else {

                                    currentStartX = bv.getStartX() + (id % 13) * (spaceWidth + (space-1));
                                    currentStartY = bv.getStartY();
                                    g.fillRect(currentStartX,currentStartY,spaceWidth, spaceHeight);

                                }
                                //visSpaces[arr.get(j).getId()] = coloredSpace;
                               // g.fillPolygon(coloredSpace);
                            }

                           // bv.repaint();
                        }
                    }
                    break;
                }
            }
        } else {
            for (int n = 1; n < visSpaces.length; n++) {
                if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    System.out.println(n + "BBBBBBBBBBBBBBBBBBBBBB");
                    board.playerMove(spaceRecord, n);
                    selected = false;
                    bv.repaint();
                    System.out.println(board);
                    break;
                }

            }
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


}
