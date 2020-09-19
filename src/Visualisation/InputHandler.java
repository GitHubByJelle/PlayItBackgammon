package src.Visualisation;

import src.World.Board;
import src.Visualisation.BoardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class InputHandler implements MouseListener {
    Shape[] visSpaces ;
    ArrayList<Ellipse2D> visPieces ;
    boolean selected=false;
    Board board;
    int spaceRecord;
    BoardView bv;
    public InputHandler(Shape [] s, ArrayList<Ellipse2D> e, BoardView bv){
        visPieces=e;
        visSpaces=s;
        board= bv.board;
        this.bv=bv;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(!selected) {
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    System.out.println(i+"AAAAAAAAAAAAAAAAAA");

                    selected=true;
                    for(int n=1;n<visSpaces.length;n++){
                        if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                            spaceRecord=n;
                        }

                    }
                    break;
                }
            }
        }else{
            for(int n=1;n<visSpaces.length;n++){
                if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    System.out.println(n+"BBBBBBBBBBBBBBBBBBBBBB");
                    board.playerMove(spaceRecord,n);
                    selected=false;
                    bv.repaint();
                    System.out.println(board);
                    break;
                }

            }
        }

//        for(int n=1;n<visSpaces.length;n++){
//                if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
//                    System.out.println("BBBBBBBBBBBBBBBBBBBBBB");
//
//                }
//
//        }
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
