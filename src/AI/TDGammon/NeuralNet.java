package AI.TDGammon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import AI.RandomBot;
import World.Board;
import World.Player;

public class NeuralNet {
    private ArrayList<TrainData> dataSet;
    private Layer[] layer;
    private float lr;

    NeuralNet(ArrayList<TrainData> dataSet, float lr){
        Neuron.range(-1, 1);
        this.dataSet= dataSet;
        this.lr = lr;

        //Network for simple functions
        this.layer= new Layer[4];

        this.layer[0]= null;
        this.layer[1]= new Layer(10,32);
        this.layer[2]= new Layer(32,32);
        this.layer[3]= new Layer(32,1);


        //small network for TD-gammon
//        layer= new Layer[5];
//        layer[0]= null;
//        layer[1]= new Layer(4*27*2,16);
//        layer[2]= new Layer(16,32);
//        layer[3]= new Layer(32,16);
//        layer[4]= new Layer(16,4);


        //Big network for TD-gammon
        // Using 25 Layers
        /*
        layer = new Layer[25];
        layer[0]= null;
        layer[1]= new Layer(dataSet.get(0).getData().length,4*27*2*2);
        //System.out.println("Leftie: " + (4*27*2) + " Rightie: " + (4*27*2*2));
        int in;
        for (in = 2; in < layer.length-1; in++){
            layer[in] = new Layer(4*27*2*2 - (in-2) * 16, 4*27*2*2 - (in-1) * 16);
            //System.out.println(i + "Leftie: " + (4*27*2*2 - (i-2) * 16) + " Rightie: " + (4*27*2*2 - (i-1) * 16));
        }
        layer[24] = new Layer(4*27*2*2 - (in-2) * 16,dataSet.get(0).getExpectedOutput().length);

        System.out.println(dataSet.size());
         */
    }

    public void forwardProp(float[] input){

        layer[0] = new Layer(input);

        for(int i=1; i<layer.length-1; i++){
            for (int j=0; j<layer[i].neuron.length; j++){
                float sum=0;
                for(int k=0; k<layer[i-1].neuron.length;k++){
                    sum+=(layer[i-1].neuron[k].value)*(layer[i].neuron[j].weights[k]);
                }
                sum += layer[i].neuron[j].bias;
                layer[i].neuron[j].value= MathUtils.reLu(sum);
                //layer[i].neuron[j].value= MathUtils.sigmoid(sum);
            }
        }
        for (int j=0; j<layer[layer.length-1].neuron.length; j++){
            float sum=0;
            for(int k=0; k<layer[layer.length-2].neuron.length;k++){
                sum+=(layer[layer.length-2].neuron[k].value)*(layer[layer.length-1].neuron[j].weights[k]);
            }
            sum += layer[layer.length-1].neuron[j].bias;
            //layer[i].neuron[j].value= MathUtils.reLu(sum);
            layer[layer.length-1].neuron[j].value= MathUtils.sigmoid(sum);
        }

    }


    void backwardProp(int data){
        int outLayer= layer.length-1; //index of the last layer

        float loss=0;
        for(int i=0; i<layer[outLayer].neuron.length; i++) {
            float predOutput = layer[outLayer].neuron[i].value;
            float target = dataSet.get(data).getTarget()[i];
            loss += MathUtils.crossEntropyLoss(predOutput, target);
        }
        loss = -loss/layer[outLayer].neuron.length;

        System.out.println("loss" + loss);

        //Update output layer for each output
        for(int i=0; i<layer[outLayer].neuron.length; i++){
            float predOutput = layer[outLayer].neuron[i].value;

            float partialDerivative = loss*MathUtils.differenceError(predOutput, dataSet.get(data).getTarget()[0]);

            layer[outLayer].neuron[i].gradient= partialDerivative;

            for(int j=0; j<layer[outLayer].neuron[i].weights.length; j++){
                float previousOutput = layer[outLayer-1].neuron[j].value;
                float error = partialDerivative*previousOutput;
                layer[outLayer].neuron[i].weightsCache[j] = layer[outLayer].neuron[i].weights[j]- this.lr
                        *error;
            }

            //Update bias
            float biasCorrection = partialDerivative*this.lr;
            layer[outLayer].neuron[i].bias -= biasCorrection;
        }

        //Update all the hidden layers
        for(int i=outLayer-1; i>0; i--){
            //for all the neurons in the layer
            for(int j=0; j<layer[i].neuron.length; j++){
                float predictedOutput = layer[i].neuron[j].value;
                float gradSum = sumGradient(j, i+1);
                float derivative = gradSum * MathUtils.differenceError(predictedOutput, dataSet.get(data).getTarget()[0]);

                layer[i].neuron[j].gradient= derivative;
                //for all weights
                for(int k=0; k<layer[i].neuron[j].weights.length; k++){
                    float prevOutput = layer[i-1].neuron[k].value;
                    float error = derivative*prevOutput;

                    layer[i].neuron[j].weightsCache[k] = layer[i].neuron[j].weights[k] - this.lr*error;
                }

                //update the bias
                float biasAdjust = derivative*this.lr;
                layer[i].neuron[j].bias -= biasAdjust;
            }
        }

        //Update all the weights
        for(int i=0; i<layer.length; i++){
            for(int j=0; j<layer[i].neuron.length; j++){
                layer[i].neuron[j].update_weight();
            }
        }
    }

    //Sum of all gradients connected to a given neuron in a layer
    private float sumGradient(int numIndex , int layerIndex ){
        float sum=0;
        Layer current= layer[layerIndex];
        for(int i=0; i<current.neuron.length; i++){
            Neuron n = current.neuron[i];
            sum += n.gradient*n.weights[numIndex];
        }
        return sum;
    }

    float[] returnOutput(float[] input){
        int numLayers= layer.length;
        int outLayer= numLayers-1; //index of the last layer
        float[] ans= new float[layer[outLayer].neuron.length];

        //forwardProp(input);
        for(int i=0; i<layer[outLayer].neuron.length; i++){
            ans[i]=layer[outLayer].neuron[i].value;
        }
        return ans;
    }

    //drops random neurons for a specific training but the neurons are active again for the next one
    void droupout(){

        float dropoutRate= 0.3f; //Rate of neurons to be zeroed/dropped out of all the neurons of the layer

        float[] neuronsDropped= new float[this.layer.length-1];

        float mult= 1/(1-dropoutRate); //multiplication factor applied to all other neurons to keep the same overall sum
        float count=0;

        Random rand = new Random();

        for (int i=0; i<this.layer.length-1; i++){
            neuronsDropped[i]=dropoutRate*this.layer[i].neuron.length;
            boolean[] dropped= new boolean[this.layer[i].neuron.length];

            //While loop to obtain the desired number of dropped neurons
            while (count<neuronsDropped[i]){
                int random_integer = rand.nextInt((this.layer[i].neuron.length-1)); //random neuron index
                if (!dropped[random_integer]) //if neuron not ye dropped --> drop it (set it to 0)
                    this.layer[i].neuron[random_integer].value=0;
                dropped[random_integer]=true;
                count++;
            }
            for(int j=0; j<this.layer[i].neuron.length; j++){
                if (!dropped[j]) //if the neuron has not been dropped apply the multiplication factor
                    this.layer[i].neuron[j].value*=mult;
            }

        }
    }

    public void setDataSet(ArrayList<TrainData> dataSet) {
        this.dataSet = dataSet;
    }

    public ArrayList<TrainData> getDataSet() {
        return dataSet;
    }

    public Layer[] getLayer() {
        return layer;
    }

    public void setLayer(Layer[] layer) {
        this.layer = layer;
    }

    public void PlayMultipleTimes(Player.Bot one, Player.Bot two, Board b, int NumberOfGames){
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

    void PlayWithRandomDie(Board b){
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

     float [] giveReward(Board b){
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

    void AddData(Board b){
        this.dataSet.add(new TrainData(new TDGdata(b).data,giveReward(b)));
    }
}


