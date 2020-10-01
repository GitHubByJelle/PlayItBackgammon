package World;

import GUI.StatusPanel;
import Visualisation.BoardView;
import World.Board;
import Utils.Variables;

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
    }
    public void setBoardView(BoardView a){
        bv=a;
    }
    public void repaintBV(){bv.repaint();}


    public void process() {
        int [] roll= board.getDie().getCurRoll();
        if (board.getDie().isDouble(roll))
            board.getDie().changeCurRoll(new int[]{roll[0], roll[0], roll[0], roll[0]});
                //if win condition not met

                //change the turn
                //new die roll
                //for each element in roll
                //prompt for a move
                //different based on player
                //if human then input listener
                //else use whatever function for calculation for the bot
                //carry out move(all checks included)
                //end turn

                //else

                System.out.print("Die: ");
                board.getDie().printCurRoll();
                checker();//finished moves per turn

               //checkEaten(k);

                if (board.checkWinCondition()) {
                    System.out.println("game over");
                }

    }

    public Player getCurrentPlayer(){
        return current;
    }
    public void setCurrentPlayer(Player a){current=a;}

    private void checkEaten(int k){
        Space s = board.getSpaces()[k];
        if(s.getSize() == 2){

            if(s.getPieces().get(0).getId() != s.getPieces().get(1).getId()){
                board.playerMove(k,0);
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
            Player cur =  getCurrentPlayer();
            System.out.println("Player: " +cur.getId() + " has finished his move");

            if (getCurrentPlayer().getId() == 0) {
                setCurrentPlayer(board.getPlayer2());
            } else {
                setCurrentPlayer(board.getPlayer1());
            }
            cur=getCurrentPlayer();
            int[] dies =board.getDie().getNextRoll();
            System.out.println("Player: " + cur.getId() + " please make move of: " + Arrays.toString(dies));
            repaintBV();
        }
    }

}
