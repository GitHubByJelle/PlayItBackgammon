package Visualisation;

import World.Board;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class InputColoring implements MouseListener {

    Shape[] visSpaces;
    ArrayList<Ellipse2D> visPieces;
    boolean selectedOver = false;
    Board board;
    int spaceColored;
    BoardView bv;

    public InputColoring(Shape[] s, ArrayList<Ellipse2D> e, BoardView bv) {
        visPieces = e;
        visSpaces = s;
        board = bv.board;
        this.bv = bv;
    }

    public boolean getSelected(int i){
        for(int j=0; j<visSpaces.length; j++){
            if(i!=j)
                return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(!selectedOver) {
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    System.out.println(i + "TTTTTTTTTTTTTTTT");

                    selectedOver = true;
                    for (int n = 1; n < visSpaces.length; n++) {
                        if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                            spaceColored = n;
                        }
                    }
                }
            }

            board.playerMovePossibilities(spaceColored);

            selectedOver=false;
            bv.repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseExited (MouseEvent mouseEvent){

    }
}
