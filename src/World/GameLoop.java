package World;


import GUI.GameOverPanel;
import Utils.Variables;
import Visualisation.BoardView;

import javax.swing.*;
import java.util.Arrays;

public class GameLoop {

    private Board board;
    private Player current;
    private BoardView bv;
    int from, to = -1;
    int validLeft = -1;
    int justValid = -1;
    JFrame frame;

    public GameLoop(Board b, JFrame frame) {
        board = b;
        current= board.getPlayer1();
        rollCheck(board.getDie().getCurRoll());
        this.frame=frame;
    }

    public GameLoop(Board b) {
        board = b;
        current= board.getPlayer1();
        rollCheck(board.getDie().getCurRoll());
    }

    public void setBoardView(BoardView a){
        bv=a;
    }
    public void repaintBV(){
        if(bv!=null)
            bv.repaint();
    }


    public void process() {
//        System.out.print("Die: ");
//        board.getDie().printCurRoll();
        checker();//finished moves per turn
        if (gameOver()) {
//            System.out.println("GAME OVER");
            if(frame!=null) {
                bv.setVisible(false);
                bv.removeStatPanel();
                Player win;
                if (board.getPlayer1().getPiecesOutOfPlay() == 15) {
                    win = board.getPlayer1();
                } else {
                    win = board.getPlayer2();
                }
                GameOverPanel over = new GameOverPanel(frame);
                over.updateResult(win.toString());
                frame.add(over);
            }else{
                if (board.getPlayer1().getPiecesOutOfPlay() == 15) {
//                    System.out.println("WINNER "+board.getPlayer1().toString());
                } else {
//                    System.out.println("WINNER "+board.getPlayer2().toString());
                }
            }

        }

    }

    public boolean gameOver(){
        return board.checkWinCondition();
    }



    public Player getCurrentPlayer(){
        return current;
    }
    public void setCurrentPlayer(Player a){current=a;}

    public void checkEaten(int k){
        Space s = board.getSpaces()[k];
        if(s.getSize() == 2){
            if(s.getPieces().get(0).getId() != s.getPieces().get(1).getId()){
                board.moveToEatenSpace(k);
                if(current==board.getPlayer1())
                    board.getPlayer2().pieceSlain();
                else
                    board.getPlayer1().pieceSlain();
//                board.updateEaten();
            }
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

    public void checker() {
        //if the player used all their moves
        if (board.getDie().getCurRoll().length ==0) {
            changeTurn();
        }else{
            current.executeTurn();
        }
    }
    public void SwitchPlayer(){
        if (getCurrentPlayer().getId() == 0) {
            setCurrentPlayer(board.getPlayer2());
        } else {
            setCurrentPlayer(board.getPlayer1());
        }
    }

    public void changeTurn(){

        if (getCurrentPlayer().getId() == 0) {
            setCurrentPlayer(board.getPlayer2());
        } else {
            setCurrentPlayer(board.getPlayer1());
        }
        board.getDie().getNextRoll();
        rollCheck(board.getDie().getCurRoll());
//        System.out.println("Player: " + current.getId() + " please make move of: " + Arrays.toString(board.getDie().getCurRoll()));
        repaintBV();
//        System.out.println("NEW TURN__________________________________________");
//        board.getDie().printCurRoll();
    }

    private void rollCheck(int[] roll) {
        //make the roll negative for player 2
        if(current== board.getPlayer2()) {
            for (int i = 0; i < roll.length; i++) {
                if (roll[i] > 0)
                    roll[i] *= -1;
            }
        }

        if (board.getDie().isDouble(roll)){
            board.getDie().changeCurRoll(new int[]{roll[0], roll[0], roll[0], roll[0]});
        }
    }

    public boolean eatenSpaceHasPieces(){
        return current.getPiecesSlain()>0;

    }

    public int getSlainSpace() {
        if(current==board.getPlayer1()){
            return 0;
        }else{
            return 25;
        }
    }

    public int getSlainSpace(int id) {
        if(id==0){
            return 0;
        }else{
            return 25;
        }
    }

}
