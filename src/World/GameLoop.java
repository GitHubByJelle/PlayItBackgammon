package World;
import Visualisation.BoardView;


import java.util.Arrays;

public class GameLoop {

    private Board board;
    private Player current;
    private BoardView bv;
    int from, to = -1;
    int validLeft = -1;
    int justValid = -1;


    public GameLoop(Board b) {
        board = b;
        current= board.getPlayer1();
        rollCheck(board.getDie().getCurRoll());
    }

    public void setBoardView(BoardView a){
        bv=a;
    }
    public void repaintBV(){bv.repaint();}


    public void process() {
        System.out.print("Die: ");
        board.getDie().printCurRoll();
        checker();//finished moves per turn
        if(current== board.getPlayer1()){
            if(board.getSpaces()[0].getSize()>0){
                //force move from out space
            }
        }


        if (board.checkWinCondition()) {
            System.out.println("game over");
        }

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

            System.out.println("Player: " +current.getId() + " has finished his move");


            changeTurn();
            int[] roll =board.getDie().getCurRoll();
            rollCheck(roll);

            System.out.println("Player: " + current.getId() + " please make move of: " + Arrays.toString(roll));

        }
    }

    public void changeTurn(){
        if (getCurrentPlayer().getId() == 0) {
            setCurrentPlayer(board.getPlayer2());
        } else {
            setCurrentPlayer(board.getPlayer1());
        }
        board.getDie().getNextRoll();
        repaintBV();
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

}
