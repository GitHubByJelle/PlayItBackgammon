package World;

import Visualisation.BoardView;

public class GameLoop {

    private Board board;
    private boolean gameOver =false;

    public GameLoop(Board b){
        board=b;
    }

    public void start(){
        while(!gameOver){
            //game start

            //check win condition (TODO:K)

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
            if(gameOver){
                //game over screen

                break;
            }
        }
    }
}
