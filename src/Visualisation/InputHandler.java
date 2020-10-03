package Visualisation;


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
    World.Player currentPlayer;


    public InputHandler(Shape[] s, ArrayList<Ellipse2D> e, BoardView bv) {
        visPieces = e;
        visSpaces = s;
        board = bv.board;
        this.bv = bv;
        currentPlayer = board.getLoop().getCurrentPlayer();

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
       // ensure the current piece and player match
        boolean z = getValidPiece(mouseEvent, board.getLoop().getCurrentPlayer());
        if (!z && !selected) {
            System.out.println("INCORRECT PIECE, cannot be moved");
            return;
        }
        //get a quadrant from the board based on mouse coordinates
        int k = convertCoordsToSpaceId(mouseEvent);
        if (!selected) {
            //if nothing is selected
            //check the empty space first
            if(board.getLoop().eatenSpaceHasPieces() &&(k!=0 ||k!=25)){
                selected=true;
                spaceRecord= board.getLoop().getSlainSpace();
                highlightValidSpaces(spaceRecord);
                System.out.println("Space selected "+k);
                System.out.println("Player must move pieces out of slain space");
            }else {
                for (int i = 0; i < visPieces.size(); i++) {
                    if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                        selected = true;
                        spaceRecord = k;
                        System.out.println("SPACE SELECTED: " + k);
                        ///recoloring part
                        highlightValidSpaces(k);

                        break;
                    }
                }
            }

        } else {
            board.playerMove(spaceRecord, k);
            selected = false;
            bv.repaint();

        }

    }

    private void highlightValidSpaces(int k) {
        int currentStartX, currentStartY;
        Graphics g = bv.getGraphics();
        g.setColor(Utils.Variables.RECOLOR_SPACES_COLOR);
        ArrayList<Space> arr = board.getValidMoves(board.getSpaces()[k]);
        for (int j = 0; j < arr.size(); j++) {
            int id = arr.get(j).getId();
            int space = bv.getSpace();
            int spaceWidth = 45;
            int spaceHeight = 216;
            if (id < 13) {
                currentStartX = bv.getStartX() + (12 - id) * (spaceWidth + space);
                currentStartY = bv.getStartY() + spaceHeight + space;
                g.fillRect(currentStartX, currentStartY, spaceWidth, spaceHeight);
                //System.out.println("inputhandler startx= "+bv.getStartX()+ " starty= "+bv.getStartY());
                // System.out.println("iIH space= "+space+ " width= "+spaceWidth+" height= "+spaceHeight);

            } else if (id < 26) {
                currentStartX = bv.getStartX() + (id % 13) * (spaceWidth + space);
                currentStartY = bv.getStartY();
                g.fillRect(currentStartX, currentStartY, spaceWidth, spaceHeight);
            } else {
                currentStartX = bv.getStartX() + spaceWidth * 16;
                currentStartY = bv.getStartY();
                g.fillRect(currentStartX, currentStartY, spaceWidth, spaceHeight * 2 + space);
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

    private int convertCoordsToSpaceId(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
       // System.out.println(x);
        if(visSpaces[0].contains(mouseEvent.getX(),mouseEvent.getY())) return 0;
        if(visSpaces[25].contains(mouseEvent.getX(),mouseEvent.getY())) return 25;
        if(visSpaces[26].contains(mouseEvent.getX(),mouseEvent.getY())) return 26;
        if(x < 765) {

            x = x + 15;
            x = x - 45;
            x = x / 60;
            x = 12 - x;
            int k = (int) x + 1;
            if (mouseEvent.getY() <= 220) {
                k = 25 - k;
            }
            //System.out.println("i: " + k);
            return k;
        }
        else return 26;
    }

    public boolean getValidPiece(MouseEvent mouseEvent, World.Player current) {
        int k = convertCoordsToSpaceId(mouseEvent);
        if(k == 26) return true;
        Space s = board.getSpaces()[k];
        if (s.getSize() >= 1) {
            return s.getPieces().get(0).getId() == current.getId();
        } else if (s.getSize() == 0) return false;
        return false;

    }


}