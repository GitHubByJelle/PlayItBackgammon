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


    public InputHandler(Shape[] s, ArrayList<Ellipse2D> e, BoardView bv) {
        visPieces = e;
        visSpaces = s;
        board = bv.board;
        this.bv = bv;
        currentPlayer = board.getLoop().getCurrentPlayer();

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //ensure the current piece and player match
//        boolean z = getValidPiece(mouseEvent, currentPlayer);
//        if (!z && !selected) {
//            System.out.println("Cant be moved");
//            return;
//        }
        //get a quadrant from the board based on mouse coordinates
        int k = convertCoordsToSpaceId(mouseEvent);
        if (!selected) {
            //if nothing is selected
            //check if the 
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    selected = true;
                    spaceRecord = k;
                    System.out.println("space number "+ k);
                    ///recoloring part
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
        if(x < 765) {

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
        else return 0;
    }

    public boolean getValidPiece(MouseEvent mouseEvent, World.Player current) {
        int k = convertCoordsToSpaceId(mouseEvent);
        if(k == 0) return true;
        Space s = board.getSpaces()[k];
        if (s.getSize() >= 1) {
            return s.getPieces().get(0).getId() == current.getId();
        } else if (s.getSize() == 0) return false;
        return false;

    }




}