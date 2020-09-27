package src.Visualisation;


import src.Utils.Variables;
import src.World.Player;
import src.World.Space;

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
    Player currentPlayer;
    public static int currentDie = 0;
    int from,to = -1;
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
        if (!selected) {
            for (int i = 0; i < visPieces.size(); i++) {
                if (visPieces.get(i).contains(mouseEvent.getX(), mouseEvent.getY())) {
                    selected = true;
                    for (int n = 1; n < visSpaces.length; n++) {
                        if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                            spaceRecord = n;
                            // System.out.println("space number "+ n);
                            from = convertCoordsToSpaceId(mouseEvent);
                            System.out.println("Something wrong: " +n);
                            ArrayList<Space> arr = board.getValidMoves(board.getSpaces()[n]);
                            for (int j = 0; j < arr.size(); j++) {

                                int currentStartX, currentStartY;
                                Polygon coloredSpace;
                                Graphics g = bv.getGraphics();
                                g.setColor(Variables.RECOLOR_SPACES_COLOR);

                                if (arr.get(j).getId() < 13) {
                                    currentStartX = bv.getStartX() + (13 - i) * (bv.getWidth() + bv.getSpace()) - bv.getSpace();
                                    currentStartY = bv.getStartY() + bv.getHeight() + bv.getSpace();
                                    coloredSpace = new Polygon(new int[]{currentStartX, currentStartX - bv.getWidth() / 2, currentStartX - bv.getWidth()}, new int[]{currentStartY + bv.getHeight(), currentStartY, currentStartY + bv.getHeight()}, 3);

                                } else {

                                    currentStartX = bv.getStartX() + (i % 13) * (bv.getWidth() + bv.getSpace());
                                    currentStartY = bv.getStartY();

                                    coloredSpace = new Polygon(new int[]{currentStartX, currentStartX + bv.getWidth(), currentStartX + bv.getWidth() / 2}, new int[]{currentStartY, currentStartY, currentStartY + bv.getHeight()}, 3);
                                }
                                visSpaces[arr.get(j).getId()] = coloredSpace;
                                g.fillPolygon(coloredSpace);
                            }

                            bv.repaint();
                        }
                    }
                    break;
                }
            }
        } else {
            for (int n = 1; n < visSpaces.length; n++) {
                if (visSpaces[n].contains(mouseEvent.getX(), mouseEvent.getY())) {
                    board.playerMove(spaceRecord, n);
                    selected = false;
                    bv.repaint();
                    currentDie++;
                    break;

                }
            }
        }
        checker();
        if(board.checkWinCondition()){
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

    private int convertCoordsToSpaceId(MouseEvent mouseEvent){
        double x = mouseEvent.getX();
        System.out.println(x);
        x = x + 15;
        x = x - 45;
        x = x/60;
        x = 12 - x;
        int k = (int)x + 1;
        if(mouseEvent.getY() <= 220 ){
            k = 25-k;
        }
        System.out.println("i: " + k);
        return k;
    }
    public boolean getValidPiece(MouseEvent mouseEvent, Player current){
        int k = convertCoordsToSpaceId(mouseEvent);
        Space s = board.getSpaces()[k];
        if(s.getSize() >= 1){
            return s.getPieces().get(0).getId() == current.getId();
        }else if(s.getSize() == 0 )return false;
        return false;
    }
    private void checker(){
        if(currentDie == 2){
            System.out.println("Player: " + currentPlayer.getId() + " has finish his move");
            if(currentPlayer.getId() == 0){
                currentPlayer = board.getPlayer2();
            }else{
                currentPlayer = board.getPlayer1();
            }
            int[] dies = board.getDie().getNextRoll();
            System.out.println("Player: " + currentPlayer.getId() + " please make move of: " + Arrays.toString(dies));
            currentDie = 0;
        }
    }
    private boolean isValidMove(int diff){
        int[] currentDies = board.getDie().getCurRoll();
        for(int i = 0; i < currentDies.length; i++){
            if(validLeft != -1 && justValid != -1){
            if( currentDies[i] == diff){
                justValid = i;
                if(i == 0){
                    validLeft = 1;
                }else{
                    validLeft = 0;
                }
                return true;
            }
                }
        }
        return false;
    }
}
