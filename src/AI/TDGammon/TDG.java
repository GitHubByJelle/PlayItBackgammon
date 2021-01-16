package AI.TDGammon;

import Utils.EasySim;
import World.Board;
import World.Player;
import World.Space;

import java.io.IOException;
import java.util.ArrayList;

import static Utils.EasySim.*;

public class TDG extends Player.Bot{
    public TDG(int id) {
        super(id);
    }
    // Import network
    private static NeuralNet neuralnet = NNFile.importNN("10k");

    // Initialise parameters
    public static boolean learningmode = false;
    public static float alpha = .1f;
    public static float lambda = .7f;

    // Initialise arrays we need
    public static float[][] sumWeightsOut = new float[neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
    public static float[][][] sumWeightsIn = new float[neuralnet.getLayer()[0].neuron.length][neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];

    // Getters
    @Override
    public String getName() {
        return "TD-Gammon";
    }

    public NeuralNet getNeuralnet(){return neuralnet;}

    // Reset initial parameters
    public void resetElig(){
        this.sumWeightsOut = new float[neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
        this.sumWeightsIn = new float[neuralnet.getLayer()[0].neuron.length][neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        moveChoice(B);
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice(Board b) {
        // Get all possible moves
        ArrayList<Space> possFrom = getPossibleFrom();
        ArrayList<Space[]> possMoves= getPossibleMoves(possFrom);
        //[FROM,TO] for all moves in possFrom

        // Get current input and output (with forward propagation)
        TDGdata cInput = new TDGdata(b);
        float[] cOutput = this.neuralnet.returnOutput(cInput.data);

        // Simulate board (for all board positions)
        EasySim.setBoardRep(b,id);

        // Initialise for next input and output
        TDGdata inputNN = null;
        float[] outputNN = null;
        // If there are possible moves execute them and see which one is the best
        if(possMoves.size()>0) {
            Space[] bestMove = possMoves.get(0);
            float valBestMove = -10000;

            float[] nOutput = new float[4];
            // For all possible moves
            for (int i = 0; i < possMoves.size(); i++) {
                // Simulate board
                simulateMove(possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());

                // Get simulated board
                Board btemp = new Board(EasySim.getBoardRep(),id,b);

                // Translate board to TDGdata
                inputNN = new TDGdata(btemp);

                // See if player already won and determine nextOuput
                if (EasySim.checkWinCondition(id))
                    // If it won, give reward
                    outputNN = NeuralNet.giveReward(EasySim.getBoardRep(), id);
                else
                    // If it didn't win, return the prediction of the next board (with forward propagation)
                    outputNN = this.neuralnet.returnOutput(inputNN.data);

                //Check if the best move
                if (btemp.getGameLoop().getCurrentPlayer().getId() == 0) {
                  // Get current value (from the temp board) and see if it's better
                    float currentBestVal = outputNN[0];
                    if (currentBestVal > valBestMove) {
                        // If it is better save it
                      valBestMove = currentBestVal;
                      bestMove = possMoves.get(i);
                        TDGdata nInput = inputNN;
                        nOutput = outputNN;
                  }

                } else if (btemp.getGameLoop().getCurrentPlayer().getId() == 1) {
                    // Get current value (from the temp board) and see if it's better
                    float currentBestVal = 1 - outputNN[0];
                    if (currentBestVal > valBestMove) {
                        // If it is better save it
                        valBestMove = currentBestVal;
                        bestMove = possMoves.get(i);
                        TDGdata nInput = inputNN;
                        nOutput = outputNN;
                    }

                }

                // Undo Move in simulation
                unDoMoveSim(possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());
            }

            // If you want to learn your bot calculate the new weights
            if (learningmode){
                NN.UpdateWeightsTD(neuralnet, alpha, lambda, cOutput, nOutput,
                        cInput.data, sumWeightsOut, sumWeightsIn);
            }

            // Make move to board with the best probability of winning for the player
            b.playerMove(bestMove[0].getId(), bestMove[1].getId());

        } else{
            // If you can't make a move pass.
            requestPassTurn();
        }
    }
}
