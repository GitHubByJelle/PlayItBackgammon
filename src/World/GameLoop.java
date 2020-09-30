package World;

import GUI.StatusPanel;
import Visualisation.BoardView;
import World.Board;
import Utils.Variables;

public class GameLoop {

    private Board board;
    private Player current;
    private boolean moveMade=false;

    public GameLoop(Board b) {
        board = b;
    }

    public void start() {
        current= board.getPlayer1();
        while (!board.checkWinCondition()) {
            //game start
                //die is prompted for a move
                board.getDie().getNextRoll();
                //if win condition not met
                if(moveMade){

                }
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


        }
    }
}
