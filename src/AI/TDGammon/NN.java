package AI.TDGammon;

import java.util.ArrayList;
import java.util.Arrays;

public class NN {


    public static void main(String[] args) {
        Neuron.range(-1, 1);

        ArrayList<TrainData> dataSet = createTrainingData();
        NeuralNet neuralNet= new NeuralNet(dataSet);

        int epoch = 20;
        float lr = 0.05f;

        for(int i=0; i<epoch; i++){
            for(int j=0; j<dataSet.size(); j++){
                neuralNet.forwardProp(dataSet.get(j).getData());
                neuralNet.backwardProp(lr, dataSet.get(j).getExpectedOutput());
            }
            ExportNN.exportNetworks(neuralNet);
        }

//        for(int i=0; i<dataSet.size(); i++){
//            System.out.println("Input " +Arrays.toString( dataSet.get(i).inputData)+" Output " +Arrays.toString(dataSet.get(i).expectedOutput));
//        }
        for(int i=0; i<neuralNet.layer.length; i++) {
            for (int j = 0; j < neuralNet.layer[i].neuron.length; j++) {
                System.out.println("weight= " + Arrays.toString(neuralNet.layer[i].neuron[j].weights));
            }
        }
    }

    public static ArrayList<TrainData> createTrainingData() {
        ArrayList<TrainData> dataSet = null;
        int dataSet_size=2000;

        // Create a whole game
/*        Board b = new Board();
        Player.Bot one= new RandomBot(0);
        Player.Bot two = new RandomBot(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();*/

        // Play a whole game and add to data
        //PlayMultipleTimes(one,two,b,10);

        for(int i=0; i<dataSet_size; i++){
            dataSet.set(i, new TrainData(new float[]{i},new float[]{2*i+8}));
        }
        return dataSet;
    }

}
