package AI.TDGammon;

import AI.RandomBot;
import World.Board;
import World.Player;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.*;

public class NN {


    public static void main(String[] args) throws IOException {
        int epoch = 100;
        float lr = 0.05f;

        ArrayList<TrainData> dataSet = createTrainingData();
        NeuralNet neuralNet = new NeuralNet(lr);

        trainData(neuralNet, epoch, dataSet);
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
        Player.Bot one= new RandomBot(0);
        Player.Bot two = new RandomBot(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();

        //Play a whole game and add to data
        NeuralNet.PlayMultipleTimes(one,two,b,10, dataSet);

        return dataSet;
    }

    public static void trainData(NeuralNet nn, int epoch, ArrayList<TrainData> dataSet) {
        for (int i = 0; i < epoch; i++) {
            for (int data = 0; data < dataSet.size(); data++) {
                nn.forwardProp(dataSet.get(data).getData());
                System.out.println("input= "+ Arrays.toString(dataSet.get(data).getData()));

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

            for( int j=0; j<nn.getLayer()[nn.getLayer().length-1].neuron.length; j++) {
                System.out.println("output= "+ nn.getLayer()[nn.getLayer().length - 1].neuron[j].value); }

        }
    }

    public static void trainDataTD(NeuralNet nn, ArrayList<TrainData> dataSet, float alpha, float lambda){
        int startK = 0;
        float[] weightChange;
        for (int t = 0; t < dataSet.size(); t++){
            float[] Yt0 = dataSet.get(t).getTarget();
            float[] Yt1 = nn.returnOutput(dataSet.get(t).getData());

            float sum = 0;
            for (int k = startK; k <= t; k++){
                int gradient = 1; //TODO not correct gradient calculation
                sum += Math.pow(lambda,t-k) * gradient;
            }
            //TODO probably not right way to do it but okay
            weightChange = ScalarFloatArr(alpha * sum,MinArray(Yt1, Yt0));

            if (ArraySum(Yt0) > 0){
                startK = t;
            }
        }
    }

    public static float[] MinArray(float[] A, float[] B){
        float[] result = new float[A.length];
        for (int i=0; i < A.length; i++){
            result[i] = A[i] - B[i];
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

    public static float ArraySum(float[] A){
        float sum = 0f;
        for (int i=0; i < A.length; i++){
            sum += A[i];
        }
        return sum;
    }

}