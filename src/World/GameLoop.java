package World;

import GUI.StatusPanel;
import Visualisation.BoardView;
import World.Board;
import Utils.Variables;

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
    public void start() {


            //game start
                //die is prompted for a move

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
                board.checker();//finished moves per turn

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

}
