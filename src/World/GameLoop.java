package World;

import Visualisation.BoardView;
import World.Board;

public class GameLoop {

    private Board board;
    private boolean gameOver = false;
    boolean isPlayer1 = false;

    public GameLoop(Board b) {
        board = b;
    }

    public void start() {
        isPlayer1 = true;
        Die die = new Die();
        while (!gameOver) {
            //game start

            //check win condition (TODO:K)
            if (!board.checkWinCondition()) {
                int[] rolls = die.getNextRoll();
                int dieIndex = 0;//  die is prompted for a move
                if (isPlayer1) {

                    isPlayer1 = false;
                } else {
                    isPlayer1 = true;
                }
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
            }
            if (gameOver) {
                //game over screen
                break;
            }
        }
    }
}
