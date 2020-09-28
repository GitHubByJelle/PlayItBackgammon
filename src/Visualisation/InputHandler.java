package Visualisation;


import World.Space;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;

public class InputHandler implements MouseListener {
    Shape[] visSpaces;
    ArrayList<Ellipse2D> visPieces;
    boolean selected = false;
    World.Board board;
    int spaceRecord;
    BoardView bv;
    Space s;
    World.Player currentPlayer;
    public static int currentDie = 0;
    int from, to = -1;
    int validLeft = -1;
    int justValid = -1;

    public InputHandler(Shape[] s, ArrayList<Ellipse2D> e, BoardView bv) {
        visPieces = e;
        visSpaces = s;
        board = bv.board;
        this.bv = bv;
        currentPlayer = board.getPlayer1();

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        boolean z = getValidPiece(mouseEvent, currentPlayer);
        if (!z && !selected) {
            System.out.println("Cant be moved");
            return;
        }
        int k = convertCoordsToSpaceId(mouseEvent);
        if (!selected) {
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    selected = true;
                    spaceRecord = k;
                    System.out.println("space number "+ k);
                    int currentStartX, currentStartY;
                    Graphics g = bv.getGraphics();
                    g.setColor(Utils.Variables.RECOLOR_SPACES_COLOR);
                    ArrayList<Space> arr = board.getValidMoves(board.getSpaces()[k]);
                    for (int j = 0; j < arr.size(); j++) {
                        int id = arr.get(j).getId();
                        int space = bv.getSpace();
                        int spaceWidth = bv.getWidth() / 18;
                        int spaceHeight = bv.getHeight() / 3;
                        if (id < 13) {
                            currentStartX = bv.getStartX() - 10 + (13 - id) * (bv.getWidth() / 20 + (space - 1)) - (space);
                            currentStartY = bv.getStartY() + spaceHeight + space;
                            g.fillRect(currentStartX, currentStartY, spaceWidth, spaceHeight + space);
                            break;
                        }
                    }
                }
            }
        } else {

            board.playerMove(spaceRecord, k);
            selected = false;
            bv.repaint();
            currentDie++;


        }
        System.out.println("Die: " + currentDie);
        checker();
        if (board.checkWinCondition()) {
            System.out.println("game over");
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

    private int convertCoordsToSpaceId(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        System.out.println(x);
        x = x + 15;
        x = x - 45;
        x = x / 60;
        x = 12 - x;
        int k = (int) x + 1;
        if (mouseEvent.getY() <= 220) {
            k = 25 - k;
        }
        System.out.println("i: " + k);
        return k;
    }

    public boolean getValidPiece(MouseEvent mouseEvent, World.Player current) {
        int k = convertCoordsToSpaceId(mouseEvent);
        Space s = board.getSpaces()[k];
        if (s.getSize() >= 1) {
            return s.getPieces().get(0).getId() == current.getId();
        } else if (s.getSize() == 0) return false;
        return false;
    }

    private void checker() {
        if (currentDie == 2) {
            System.out.println("Player: " + currentPlayer.getId() + " has finish his move");
            if (currentPlayer.getId() == 0) {
                currentPlayer = board.getPlayer2();
            } else {
                currentPlayer = board.getPlayer1();
            }
            int[] dies = board.getDie().getNextRoll();
            System.out.println("Player: " + currentPlayer.getId() + " please make move of: " + Arrays.toString(dies));
            currentDie = 0;
        }
    }

    private boolean isValidMove(int diff) {
        int[] currentDies = board.getDie().getCurRoll();
        for (int i = 0; i < currentDies.length; i++) {
            if (validLeft != -1 && justValid != -1) {
                if (currentDies[i] == diff) {
                    justValid = i;
                    if (i == 0) {
                        validLeft = 1;
                    } else {
                        validLeft = 0;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
