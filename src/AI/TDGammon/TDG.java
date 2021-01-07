package AI.TDGammon;

import World.Board;
import World.Player;
import World.Space;

import java.util.ArrayList;
import java.util.Random;

public class TDG extends Player.Bot{
    public TDG(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "TD-Gammon";
    }

    public static void main(String[] args){
        Board b = new Board();
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        //moveChoice();
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice(Board b) {
        // Get all possible moves
        ArrayList<Space> possFrom = getPossibleFrom();
        ArrayList<Space[]> possMoves= getPossibleMoves(possFrom);
        //[FROM,TO] for all moves in possFrom

        // For all possible moves do:
        TDGdata inputNN;
        Space[] bestMove;
        float valBestMove = -1; // Forward propagation should always return a value between 0 and 1.
        int multiplier = 2;
        for (int i = 0; i < possMoves.size(); i++){
            // Execute the move
            //MakeMove(b,FROM,TO);

            // Translate board to TDGdata
            inputNN = new TDGdata(b);

            // Use forward propagation to predict
            //float[] outputNN = NeuralNet.forwardProp(inputNN.data);

            // Check if the best move
            // if (currentPlayerID == 0){
            //      float currentBestVal = outputNN[0] > outputNN[1]*multiplier ? outputNN[0] : outputNN[1]*multiplier;
            //      if (currentBestVal > valBestMove){
            //          valBestMove = currentBestVal;
            //          bestMove = possMoves.get(i)
            //      }
            // } else if (currentPlayerID == 1){
            //            float currentBestVal = outputNN[2] > outputNN[3]*multiplier ? outputNN[2] : outputNN[3]*multiplier;
            //            if (currentBestVal > valBestMove){
            //                  valBestMove = currentBestVal;
            //                  bestMove = possMoves.get(i)
            //            }
            // }

            // Undo Move
            // MakeMove(b,TO,FROM);

        }
        // Make move to board with the best probability of winning for the player
        //MakeMove(b,bestFrom,bestTo);
    }
}
