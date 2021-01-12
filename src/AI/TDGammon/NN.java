package AI.TDGammon;

import AI.RandomBot;
import World.Board;
import World.Player;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.*;

import static AI.BotTestingGround.testWithRandomDie;

public class NN {


    public static void main(String[] args) throws IOException {
        int epoch = 100;
        float lr = 0.05f;

        ArrayList<TrainData> dataSet = createTrainingData();
        NeuralNet neuralNet = new NeuralNet(lr);

        neuralNet = ImportNN.importNet("target_network");

        trainDataTD3(neuralNet, 1, 0.1f, 0.7f);

        ExportNN.exportNetworks(neuralNet);
    }

    public static  ArrayList<TrainData> createTrainingData() throws IOException {
        ArrayList<TrainData> dataSet = new ArrayList<>();
   /*     ArrayList<int []> data = new ArrayList<>();

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/adagrund/Downloads/assignment-3-AdaGrund-master/PlayItBackgammon /src/AI/TDGammon/housepricedata.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int[] myIntArray = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                data.add(myIntArray);
            }
        }

        for(int i = 0; i < data.size(); i++) {
            int[] ins = data.get(i);
            float[] bref = new float[ins.length-1];

            for (int k = 0; k < ins.length-1; k++) {
                bref[k] = (float) ins[k];
            }

            dataSet.add(i, new TrainData(bref, new float[]{data.get(i)[data.get(i).length - 1]}));
        }
*/
//        int dataSet_size = 100;
//
//        Random r = new Random();
//
//        for (int i = 0; i < dataSet_size; i++)
//           dataSet.add(i, new TrainData(new float[]{i}, new float[]{(float) 3*i}));
//       }
//
//        Collections.shuffle(dataSet);
//
//        dataSet.add(0, new TrainData(new float[]{0, 0}, new float[]{0}));
//        dataSet.add(1, new TrainData(new float[]{0, 1}, new float[]{1}));
//        dataSet.add(2, new TrainData(new float[]{1, 1}, new float[]{0}));
//        dataSet.add(3, new TrainData(new float[]{1, 0}, new float[]{1}));
//        return dataSet;

        // Create a whole game
        Board b = new Board();
        Player.Bot one= new TDG(0);
        Player.Bot two = new TDG(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();

        //Play a whole game and add to data
        NeuralNet.PlayMultipleTimes(one,two,b,10, dataSet);

        return dataSet;
    }

    public static  ArrayList<TrainData> createGameSequence(){
        ArrayList<TrainData> dataSet = new ArrayList<>();

        // Create a whole game
        Board b = new Board();
        Player.Bot one= new TDG(0);
        Player.Bot two = new TDG(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();

        //Play a whole game and add to data
        NeuralNet.PlayMultipleTimes(one,two,b,1, dataSet);

        return dataSet;
    }

    public static void trainData(NeuralNet nn, int epoch, ArrayList<TrainData> dataSet) {
        for (int i = 0; i < epoch; i++) {
            for (int data = 0; data < dataSet.size(); data++) {
                nn.forwardProp(dataSet.get(data).getData());
                System.out.println("input= " + Arrays.toString(dataSet.get(data).getData()));

//                Layer[] layer = nn.getLayer();
//                for (int l = 0; l < layer.length; l++) {
//                    for (int m = 0; m < layer[l].neuron.length; m++) {
//                        System.out.println("weight B:" + Arrays.toString(layer[l].neuron[m].weights));
//                    }
//                }

                nn.backwardProp(data);
//
//                layer = nn.getLayer();
//                for (int l = 0; l < layer.length; l++) {
//                    for (int m = 0; m < layer[l].neuron.length; m++) {
//                        System.out.println("weight A:" + Arrays.toString(layer[l].neuron[m].weights));
//                    }
//                }

            }
            //System.out.println("expectedOut= "+ Arrays.toString(nn.getDataSet().get(data).getTarget()));

            for (int j = 0; j < nn.getLayer()[nn.getLayer().length - 1].neuron.length; j++) {
                System.out.println("output= " + nn.getLayer()[nn.getLayer().length - 1].neuron[j].value);
            }

        }
    }


    public static void trainDataTD3(NeuralNet nn, int NumberOfEpochs, float alpha, float lambda) {
        for (int ep = 0; ep < NumberOfEpochs; ep++) {
            System.out.println("Game: " + ep + ".");
            ArrayList<TrainData> dataSet = createGameSequence();
            int t;

            ArrayList<TrainData> dataSet2 = new ArrayList<>();
            for (int num = 0; num < 10; num++){
                dataSet2.add(dataSet.get(num));
            }

            dataSet2.add(dataSet.get(dataSet.size()-1));
            dataSet = dataSet2;

            for (t = 0; t < dataSet.size() - 1; t++) {
                // Start at the end and get output from NN (Layer 2 -> Layer 3 a.k.a. Hidden -> Output)
                float[] Yt0 = nn.returnOutput(dataSet.get(t).getData());
                float[] Yt1 = nn.returnOutput(dataSet.get(t + 1).getData());

                // Update weight with next prediction
                UpdateWeightsTD(nn, alpha, lambda, Yt0, Yt1, dataSet, t);
            }

            // Last Layer uses final reward instead of next prediction
            float[] Yt0 = nn.returnOutput(dataSet.get(t).getData());
            float[] z = dataSet.get(t).getTarget();

            // Update weight with final reward
            UpdateWeightsTD(nn, alpha, lambda, Yt0, z, dataSet, t);

            System.out.println();
        }
    }

    public static void UpdateWeightsTD(NeuralNet nn, float alpha, float lambda, float[] Yt0, float[] Yt1,
                                           ArrayList<TrainData> dataSet, int t){

        float[][] weightChangeIn = new float[nn.getLayer()[1].neuron.length][nn.getLayer()[0].neuron.length];
        float[][] weightChangeOut = new float[nn.getLayer()[2].neuron.length][nn.getLayer()[1].neuron.length];
        for (int o = 0; o < nn.getLayer()[2].neuron.length; o++) {
            for (int i = 0; i < nn.getLayer()[1].neuron.length; i++) {
                for (int j = 0; j < nn.getLayer()[0].neuron.length; j++) {
                    float sum = 0;
                    for (int k = 0; k <= t; k++) {
                        float[] Yk0 = nn.returnOutput(dataSet.get(k).getData());
                        float[] Yki = nn.returnHiddenVal(dataSet.get(k).getData());
                        float[] xjk = dataSet.get(k).getData();
                        float w = nn.getLayer()[2].neuron[o].weights[i];
                        float gradient = MathUtils.sigmoidDerivative(Yk0[o]) *
                                w * MathUtils.sigmoidDerivative(Yki[i]) *
                                xjk[j];
                        sum += (float) Math.pow(lambda, t - k) * gradient;
                    }

                    float wDiffij = alpha * (Yt1[o] - Yt0[o]) * sum;
                    weightChangeIn[i][j] = nn.getLayer()[1].neuron[i].weights[j] + wDiffij;
                }

                // Output Layer (Hidden Layer -> Output Layer)
                float sum = 0;
                for (int k = 0; k <= t; k++) {
                    float[] Yk0 = nn.returnOutput(dataSet.get(k).getData());
                    float[] Yki = nn.returnHiddenVal(dataSet.get(k).getData());
                    float gradient = MathUtils.sigmoidDerivative(Yk0[o]) * Yki[o];
                    sum += (float) Math.pow(lambda, t - k) * gradient;
                }

                float wDiffij = alpha * (Yt1[o] - Yt0[o]) * sum;
                weightChangeOut[o][i] = nn.getLayer()[2].neuron[o].weights[i] + wDiffij;
            }
        }

        // Start updating weights
        System.out.println("Start updating weights. Sequence " + (t+1) + " of " + dataSet.size() + ". Weights: " +
                (nn.getLayer()[2].neuron[0].weights[0]) + " " + (nn.getLayer()[2].neuron[1].weights[0]));
        nn.UpdateWeightsTo(weightChangeIn,1);
        nn.UpdateWeightsTo(weightChangeOut,2);
    }


    public static void TrainTDGammon(NeuralNet nn, int NumberOfEpochs, float alpha, float lambda){
        Board b = new Board();
        TDG one = new TDG(0);
        TDG two = new TDG(1);
        for(int i = 0; i<NumberOfEpochs; i++){
            b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie();
        }
    }


public static float[] MinArray(float[] A, float[] B){
    float[] result = new float[A.length];
    for (int i=0; i < A.length; i++){
        result[i] = A[i] - B[i];
    }
    return result;
}

public static float[] AddArray(float[] A, float[] B){
    float[] result = new float[A.length];
    for (int i=0; i < A.length; i++){
            result[i] = A[i] + B[i];
        }
        return result;
    }

    public static float[] ScalarFloatArr(float s, float[] a){
        float[] returnarr = new float[a.length];
        for(int i = 0; i<a.length; i++){
            returnarr[i] = a[i]*s;
        }
        return returnarr;
    }

    public static float[][] ScalarFloatMat(float s, float[][] a) {
        float[][] returnarr = new float[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                returnarr[i][j] = a[i][j]*s;
            }
        }
        return returnarr;
    }

    public static float ArraySum(float[] A){
        float sum = 0f;
        for (int i=0; i < A.length; i++){
            sum += A[i];
        }
        return sum;
    }

    public static float[] VectorSigmoidDerivative(float[] A){
        float[] result = new float[A.length];
        for (int i=0; i < A.length; i++){
            result[i] += A[i] * (1-A[i]);
        }
        return result;
    }

    public static float[][] matrixMultiplication(float[] A, float[] B){
        float[][] ans = new float[A.length][B.length];

        for(int i=0; i<A.length; i++){
            for(int j=0; j<B.length;j++){
                ans[i][j]=A[i]*B[j];
            }
        }
        return ans;
    }

}