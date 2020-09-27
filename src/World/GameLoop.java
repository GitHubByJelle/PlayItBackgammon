package World;

import GUI.StatusPanel;
import Visualisation.BoardView;
import World.Board;
import Utils.Variables;

public class GameLoop {

    private Board board;
    private boolean gameOver = false;
    private Player current;
    public GameLoop(Board b) {
        board = b;
    }

    public void start() {
        current= board.getPlayer1();
        while (!gameOver) {
            //update status

            //game start

            //check win condition (TODO:K)
            if (!board.checkWinCondition()) {
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
            }else{
                gameOver=true;
            }
            if (gameOver) {
                //game over screen
                break;
            }
        }
    }
}
