package AI.TDGammon;

import AI.GA.TMM;
import World.Board;
import World.Player;
import World.Space;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static Utils.EasySim.simulateMove;
import static Utils.EasySim.unDoMoveSim;

public class TDG extends Player.Bot{
    public TDG(int id) {
        super(id);
    }
    private static NeuralNet neuralnet = NNFile.importNN("newtest"); //new NeuralNet(.1f);

    public boolean learningmode = false;
    public static float[][] Ew = new float[neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
    public static float[][][] Ev = new float[neuralnet.getLayer()[0].neuron.length][neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];

    public NeuralNet getNeuralnet(){return neuralnet;}

    public void resetElig(){
        this.Ew = new float[neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
        this.Ev = new float[neuralnet.getLayer()[0].neuron.length][neuralnet.getLayer()[1].neuron.length][neuralnet.getLayer()[2].neuron.length];
    }

    @Override
    public String getName() {
        return "TD-Gammon";
    }

    public static void main(String[] args) throws IOException {
        Board b = new Board();

        Player one = new TDG(0);
        Player two = new TMM(1);
        one.resetPlayer();
        two.resetPlayer();
        b.setPlayers(one,two);
        b.createBotLoop();
        ((TDG) one).moveChoice(b);
        System.out.println("Yep, I finally found it.");
        System.out.println(b.toString());
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

        // For all possible moves do:
        TDGdata cInput = new TDGdata(b);
        float[] cOutput = this.neuralnet.returnOutput(cInput.data);

        TDGdata inputNN = null;
        float[] outputNN = null;
        if(possMoves.size()>0) {
            Space[] bestMove = possMoves.get(0);
            float valBestMove = -1; // Forward propagation should always return a value between 0 and 1.
            int multiplier = 1;
            for (int i = 0; i < possMoves.size(); i++) {
                // Execute the move
                simulateMove(getBoardRepresentation(b), possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());

                // Translate board to TDGdata
                inputNN = new TDGdata(b);

                // Use forward propagation to predict
                outputNN = this.neuralnet.returnOutput(inputNN.data);
                //Check if the best move
                if (b.getGameLoop().getCurrentPlayer().getId() == 0) {
                  float currentBestVal = outputNN[0] > outputNN[1]*multiplier ? outputNN[0] : outputNN[1]*multiplier;
                  if (currentBestVal > valBestMove) {
                      valBestMove = currentBestVal;
                      bestMove = possMoves.get(i);
                  }

                } else if (b.getGameLoop().getCurrentPlayer().getId() == 1) {
                    float currentBestVal = outputNN[2] > outputNN[3] * multiplier ? outputNN[2] : outputNN[3] * multiplier;
                    if (currentBestVal > valBestMove) {
                        valBestMove = currentBestVal;
                        bestMove = possMoves.get(i);
                    }

                }
                // Undo Move
                unDoMoveSim(getBoardRepresentation(b), possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());
            }
            // Make move to board with the best probability of winning for the player
            b.playerMove(bestMove[0].getId(), bestMove[1].getId());

            if (learningmode){
                TDGdata nInput = new TDGdata(b);
                float[] nOutput;
                if (b.checkWinCondition())
                    nOutput = NeuralNet.giveReward(b);
                else
                    nOutput = this.neuralnet.returnOutput(nInput.data);

                System.out.println(Arrays.toString(cOutput));
                System.out.println(Arrays.toString(nOutput));
//
//                System.out.println(Arrays.toString(Ev[4][0]));

                TrainData data1 = new TrainData(cInput.data,cOutput);
                TrainData data2 = new TrainData(nInput.data,nOutput);
                ArrayList<TrainData> dataSet = new ArrayList<>();
                dataSet.add(data1); dataSet.add(data2);

                NN.UpdateWeightsTD2(neuralnet, 0.1f, 0.7f, cOutput, nOutput, dataSet, 0,Ew,Ev);

//                System.out.println(Arrays.toString(Ev[4][0]));
               System.out.println();
            }

        } else{
            requestPassTurn();
        }
    }
    private int [][] getBoardRepresentation(Board b){
        int[][] board = new int[24][3];//{id,num pieces this player, num pieces opp}
        for(int i=0;i<board.length;i++){
            board[i][0]=b.getSpaces()[i+1].getId();
            if(!b.getSpaces()[i+1].isEmpty() && b.getSpaces()[i+1].getPieces().get(0).getId()==id)
                board[i][1]=b.getSpaces()[i+1].getSize();
            else
                board[i][1]=0;

            if(!b.getSpaces()[i+1].isEmpty() && b.getSpaces()[i+1].getPieces().get(0).getId()!=id)
                board[i][2]=b.getSpaces()[i+1].getSize();
            else
                board[i][2]=0;


        }
        return board;
    }

}
