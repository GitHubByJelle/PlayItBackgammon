package AI.TDGammon;

import java.util.ArrayList;
import java.util.Arrays;

import AI.RandomBot;
import World.Board;
import World.Player;

public class NeuralNet {

    static Layer[] layer;
    static ArrayList<TrainData> dataSet = new ArrayList<TrainData>();

    public static void main(String[] args) {
        Neuron.range(-1, 1);

        layer= new Layer[5];
        layer[0]= null;
        layer[1]= new Layer(4*27*2,16);
        layer[2]= new Layer(16,32);
        layer[3]= new Layer(32,16);
        layer[4]= new Layer(16,4);

        createTrainingData();

        // Using 25 Layers
//        layer = new Layer[25];
//        layer[0]= null;
//        layer[1]= new Layer(dataSet.get(0).getData().length,4*27*2*2);
//        //System.out.println("Leftie: " + (4*27*2) + " Rightie: " + (4*27*2*2));
//        int in;
//        for (in = 2; in < layer.length-1; in++){
//            layer[in] = new Layer(4*27*2*2 - (in-2) * 16, 4*27*2*2 - (in-1) * 16);
//            //System.out.println(i + "Leftie: " + (4*27*2*2 - (i-2) * 16) + " Rightie: " + (4*27*2*2 - (i-1) * 16));
//        }
//        layer[24] = new Layer(4*27*2*2 - (in-2) * 16,dataSet.get(0).getExpectedOutput().length);
//        //System.out.println("Leftie: " + (4*27*2*2 - (i-2) * 16) + " Rightie: " + 4);

        System.out.println(dataSet.size());

        trainData(20, 0.05f);

//        for(int i=0; i<dataSet.size(); i++){
//            System.out.println("Input " +Arrays.toString( dataSet.get(i).inputData)+" Output " +Arrays.toString(dataSet.get(i).expectedOutput));
//        }
        for(int i=0; i<layer.length; i++) {
            for (int j = 0; j < layer[i].neuron.length; j++) {
                System.out.println("weight= " + Arrays.toString(layer[i].neuron[j].weights));
            }
        }
    }

    public static void forwardProp(float[] input){

        layer[0]= new Layer(input);

        for(int i=1; i<layer.length; i++){
            for (int j=0; j<layer[i].neuron.length; j++){
                float sum=0;
                for(int k=0; k<layer[i-1].neuron.length;k++){
                    sum+=(layer[i-1].neuron[k].value)*(layer[i].neuron[j].weights[k]);
                }
//                layer[i].neuron[j].value= MathUtils.reLu(sum);
                layer[i].neuron[j].value= MathUtils.sigmoid(sum);
            }
        }

    }


    public static void backwardProp(float learningRate, float[] ExpectedOutput){
        int numLayers= layer.length;
        int outLayer= numLayers-1; //index of the last layer

        //Update output layer for each output
        for(int i=0; i<layer[outLayer].neuron.length; i++){
            float predOutput= layer[outLayer].neuron[i].value;
            float target= ExpectedOutput[i];
            float derivative= predOutput-target;
            float delta= derivative*(predOutput*(1-predOutput));
            layer[outLayer].neuron[i].gradient= delta;
            for(int j=0; j<layer[outLayer].neuron[i].weights.length; j++){
                float previousOutput= layer[outLayer-1].neuron[j].value;
                float error= delta*previousOutput;
                layer[outLayer].neuron[i].weightsCache[j]= layer[outLayer].neuron[i].weights[j]- learningRate*error;
            }
        }

        //Update all the hidden layers
        for(int i=outLayer-1; i>0; i--){
            //for all the neurons in the layer
            for(int j=0; j<layer[i].neuron.length; j++){
                float predOutput= layer[i].neuron[j].value;
                float gradSum= sumGradient(j, i+1);
                float delta= (gradSum)*(predOutput*(1-predOutput));
                layer[i].neuron[j].gradient= delta;
                //for all weights
                for(int k=0; k<layer[i].neuron[j].weights.length; k++){
                    float prevOutput= layer[i-1].neuron[k].value;
                    float error= delta*prevOutput;
                    layer[i].neuron[j].weightsCache[k]= layer[i].neuron[j].weights[k] - learningRate*error;
                }
            }
        }

        //Update all the weights
        for(int i=0; i<layer.length; i++){
            for(int j=0; j<layer[i].neuron.length; j++){
                layer[i].neuron[j].update_weight();
            }
        }
    }

    private static void createTrainingData() {
        //int dataSet_size=2000;
        // Create a whole game
        Board b = new Board();
        Player.Bot one= new RandomBot(0);
        Player.Bot two = new RandomBot(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();

        // Play a whole game and add to data
        PlayMultipleTimes(one,two,b,10);

//        for(int i=0; i<dataSet_size; i++){
//            dataSet[i]=new TrainData(new float[]{i},new float[]{2*i+8});
//        }
    }

    private static void trainData( int epoch, float learningRate){
        for(int i=0; i<epoch; i++){
            for(int j=0; j<dataSet.size(); j++){
                forwardProp(dataSet.get(j).getData());
                backwardProp(learningRate, dataSet.get(j).getExpectedOutput());
            }
        }
    }

    //Sum of all gradients connected to a given neuron in a layer
    public static float sumGradient(int numIndex , int layerIndex ){
        float sum=0;
        Layer current= layer[layerIndex];
        for(int i=0; i<current.neuron.length; i++){
            Neuron n = current.neuron[i];
            sum+= n.gradient*n.weights[numIndex];
        }
        return sum;
    }

    public static void PlayMultipleTimes(Player.Bot one, Player.Bot two, Board b, int NumberOfGames){
        for(int i = 0; i<NumberOfGames; i++){
            b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            PlayWithRandomDie(b);
//            System.out.println(b);
        }
    }

    private static void PlayWithRandomDie(Board b){
        AddData(b);
        b.getDie().getDieList().clear();
        b.getDie().generateDie();
        b.getDie().getNextRoll();
        while(!b.checkWinCondition()){
//            System.out.println(b);
            b.getGameLoop().process();
            AddData(b);
//            System.out.println(Arrays.toString(b.getDie().getCurRoll()));
        }
    }

    private static float [] giveReward(Board b){
        float [] win= new float[4];//{player 0 win, player 0 gammon, player 1 win, player 1 gammon}
        if(b.getPlayer1().getPiecesOutOfPlay()==15){
            if(b.getPlayer2().getPiecesOutOfPlay()==0){
                win[1]=1f;
            }else{
                win[0]=1f;
            }
        } else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
            if(b.getPlayer1().getPiecesOutOfPlay()==0){
                win[3]=1f;
            }else{
                win[2]=1f;
            }
        }
        return win;
    }

    private static void AddData(Board b){
        dataSet.add(new TrainData(new TDGdata(b).data,giveReward(b)));
    }
}


