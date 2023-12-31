package AI.TDGammon;

import AI.BotTestingGround;
import AI.GA.TMM;
import AI.RandomBot;
import World.Board;
import World.Player;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class NN {


    public static void main(String[] args) {
        trainDataTD(2000000, true, 900);
    }

    public static void trainDataTD(int NumberOfGames, boolean Save, int offSet){
        // For every game
        // Create a whole game
        Board b = new Board();
        TDG one = new TDG(0);
        TDG two = new TDG(1);
        one.pausing=false;
        two.pausing=false;
        one.learningmode = true;
        two.learningmode = true;
        b.setPlayers(one,two);
        b.createBotLoop();

        for (int ep = 0; ep < NumberOfGames; ep++) {
            // Keeping track of time (for printing)
            long time = System.nanoTime();

            // Reset everything for TDG
            b = new Board();
            one.resetElig();
            two.resetElig();

            //Play a whole game and add to data
            NeuralNet.PlayGame(one, two, b);

            // Printing for indication
            time = System.nanoTime() - time;
            double GPS = (1 / (time / 1000000000.));
            double TL = (NumberOfGames - ep) / GPS / 60;
            System.out.format("Completed game: %d (of %d). Games/s: %.2f. Time remaining [min]: %.2f\n",
                    ep + 1, NumberOfGames, GPS, TL);

            // Saving the NN every 10k
            if (Save) {
                if ((ep + 1) % 50000 == 0) {
                    NNFile.export(one.getNeuralnet(), ((ep + 1) / 1000 + offSet) + "k");
                }
            }
        }
    }

    public static void UpdateWeightsTD(NeuralNet nn, float alpha, float lambda, float[] Yt0, float[] Yt1,
                                       float[] TDBoard, float[][] sumWeightsOut, float[][][] sumWeightsIn){

        // First we will calculate the sum part of the RHS, such that we don't have to loop over it every time step,
        // but reuse our previous calculations. We can do this by using for loops. After that we will calculate the
        // difference in the Y values (Yt1 - Yt0, a.k.a. nextOutput - currentOutput, where nextOutput is the nextPrediction
        // or is equal to the reward at the last move). If we calculated all part of the RHS we will actually update
        // the weights.

        nn.forwardProp(TDBoard);
        // Calculating and saving the sum part of RHS
        for (int j = 0; j < nn.getLayer()[1].neuron.length; j++) {
            for (int k = 0; k < nn.getLayer()[2].neuron.length; k++) {
                // Calculate for the input part (Layer 1 -> Layer 2, a.k.a. Input -> Hidden)
                for (int i = 0; i < nn.getLayer()[0].neuron.length; i++) {
                    sumWeightsIn[i][j][k] = (lambda * sumWeightsIn[i][j][k]) +
                            (MathUtils.sigmoidDerivative(nn.getLayer()[2].neuron[k].value) * nn.getLayer()[2].neuron[k].weights[j] *
                                    MathUtils.sigmoidDerivative(nn.getLayer()[1].neuron[j].value) * TDBoard[i]);
                }
                // Calculate for the output part (Layer 2 -> Layer 3, a.k.a. Hidden -> Output)
                sumWeightsOut[j][k] = (lambda * sumWeightsOut[j][k]) +
                        MathUtils.sigmoidDerivative(nn.getLayer()[2].neuron[k].value) * nn.getLayer()[1].neuron[j].value;
            }
        }

        // Calculating the difference RHS (part before the sum)
        float[] YDiff = new float[Yt0.length];
        for (int k = 0; k < Yt0.length; k++){
            YDiff[k] = Yt1[k] - Yt0[k];
        }

        // Calculating and updating the differences of the weights
        for (int j = 0; j < nn.getLayer()[1].neuron.length; j++){
            for (int k = 0; k < YDiff.length; k++){
                // Change for the input part (Layer 1 -> Layer 2, a.k.a. Input -> Hidden)
                for (int i = 0; i < nn.getLayer()[0].neuron.length; i++){
                    nn.getLayer()[1].neuron[j].weights[i] += alpha * YDiff[k] * sumWeightsIn[i][j][k];
                }
                // Change for the output part (Layer 2 -> Layer 3, a.k.a. Hidden -> Output)
                nn.getLayer()[2].neuron[k].weights[j] += alpha * YDiff[k] * sumWeightsOut[j][k];
            }
        }
    }
}