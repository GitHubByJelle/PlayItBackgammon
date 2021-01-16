package AI.TDGammon;

import AI.GA.TMM;
import Utils.EasySim;
import World.Board;
import World.Player;
import World.Space;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static Utils.EasySim.*;

public class TDG extends Player.Bot{
    public TDG(int id) {
        super(id);
    }
    private static NeuralNet neuralnet = NNFile.importNN("nn2"); //new NeuralNet(.1f);
    //private static NeuralNet neuralnet = ImportNN.importNet("nn1"); //new NeuralNet(.1f);
    //private static NeuralNet neuralnet = new NeuralNet(.1f);

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
        Player two = new TDG(1);
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
//        System.out.println(b);
//        System.out.println("Die:");
//        b.getDie().printCurRoll();
//        System.out.println();
        // Get all possible moves
        ArrayList<Space> possFrom = getPossibleFrom();
        ArrayList<Space[]> possMoves= getPossibleMoves(possFrom);
        //[FROM,TO] for all moves in possFrom

        // For all possible moves do:
        TDGdata cInput = new TDGdata(b);
        float[] cOutput = this.neuralnet.returnOutput(cInput.data);

        EasySim.setBoardRep(b,id);

        TDGdata inputNN = null;
        float[] outputNN = null;
        if(possMoves.size()>0) {
            Space[] bestMove = possMoves.get(0);
            float valBestMove = -10000; // Forward propagation should always return a value between 0 and 1.
            int multiplier = 1;
//            System.out.println("Let's Try ALLLLL moves");

            float[] nOutput = new float[4];
            for (int i = 0; i < possMoves.size(); i++) {
//                System.out.println("From: " + possMoves.get(i)[0].getId() + "-> To: " + possMoves.get(i)[1].getId());
                // Execute the move
                simulateMove(possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());

                Board btemp = new Board(EasySim.getBoardRep(),id,b);
                // Translate board to TDGdata
                inputNN = new TDGdata(btemp);

                // Use forward propagation to predict
                //outputNN = this.neuralnet.returnOutput(inputNN.data);
//                System.out.println(btemp.getGameLoop().getCurrentPlayer().getPiecesOutOfPlay());
                if (EasySim.checkWinCondition(id)) {
//                    System.out.println(btemp.getOutOfPlay());
                    outputNN = NeuralNet.giveReward(EasySim.getBoardRep(), id);
//                    System.out.println("Yeah got reward: " + outputNN[0]);
                }
                else
                    outputNN = this.neuralnet.returnOutput(inputNN.data);

//                System.out.println(btemp);
//                System.out.println("Output: " + outputNN[0]);
                //Check if the best move
                if (btemp.getGameLoop().getCurrentPlayer().getId() == 0) {
                  //float currentBestVal = outputNN[0] > outputNN[1]*multiplier ? outputNN[0] : outputNN[1]*multiplier;
                    float currentBestVal = outputNN[0];
                    if (currentBestVal > valBestMove) {
                      valBestMove = currentBestVal;
                      bestMove = possMoves.get(i);
                        TDGdata nInput = inputNN;
                        nOutput = outputNN;
                  }

                } else if (btemp.getGameLoop().getCurrentPlayer().getId() == 1) {
                    //float currentBestVal = outputNN[2] > outputNN[3] * multiplier ? outputNN[2] : outputNN[3] * multiplier;
                    float currentBestVal = 1 - outputNN[0];
                    if (currentBestVal > valBestMove) {
                        valBestMove = currentBestVal;
                        bestMove = possMoves.get(i);
                        TDGdata nInput = inputNN;
                        nOutput = outputNN;
                    }

                }
                // Undo Move
                unDoMoveSim(possMoves.get(i)[0].getId(), possMoves.get(i)[1].getId());
            }

            if (learningmode){
//                TDGdata nInput = new TDGdata(b);
//                if (b.checkWinCondition())
//                    nOutput = NeuralNet.giveReward(b);
//                else
//                    nOutput = this.neuralnet.returnOutput(nInput.data);

//                System.out.println(Arrays.toString(cOutput));
//                System.out.println(Arrays.toString(nOutput));

//                System.out.println(Arrays.toString(Ev[4][0]));

                TrainData data1 = new TrainData(cInput.data,cOutput);
//                TrainData data2 = new TrainData(nInput.data,nOutput);
                ArrayList<TrainData> dataSet = new ArrayList<>();
                dataSet.add(data1); //dataSet.add(data2);

                NN.UpdateWeightsTD2(neuralnet, 0.1f, 0.7f, cOutput, nOutput, dataSet, 0,Ew,Ev);

//                System.out.println(Arrays.toString(Ev[4][0]));
//               System.out.println();
            }

            //System.out.println("MAKE THE ACTUAL MOVEEEEEEE");
            // Make move to board with the best probability of winning for the player
            b.playerMove(bestMove[0].getId(), bestMove[1].getId());

        } else{
            requestPassTurn();
            //System.out.println("TD PASSED!");
        }
//        System.out.println("#####################################################");
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
