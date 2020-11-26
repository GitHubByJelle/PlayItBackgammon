package AI.TDGammon;

import java.util.Arrays;

public class NeuralNet {

    static Layer[] layer;
    static TrainData[] dataSet;

    public static void main(String[] args) {
        Neuron.range(-1, 1);

        layer= new Layer[5];
        layer[0]= null;
        layer[1]= new Layer(1,16);
        layer[2]= new Layer(16,32);
        layer[3]= new Layer(32,16);
        layer[4]= new Layer(16,1);

        createTrainingData();
        trainData(20, 0.05f);

        for(int i=0; i<dataSet.length; i++){
            System.out.println("Input " +Arrays.toString( dataSet[i].inputData)+" Output " +Arrays.toString(dataSet[i].expectedOutput));
        }
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
                layer[i].neuron[j].value= MathUtils.reLu(sum);
            }
        }

    }


    public static void backwardProp(float learningRate, TrainData data){
        int numLayers= layer.length;
        int outLayer= numLayers-1; //index of the last layer

        //Update output layer for each output
        for(int i=0; i<layer[outLayer].neuron.length; i++){
            float predOutput= layer[outLayer].neuron[i].value;
            float target= data.expectedOutput[i];
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
        int dataSet_size=2000;
        dataSet= new TrainData[dataSet_size];

        for(int i=0; i<dataSet_size; i++){
            dataSet[i]=new TrainData(new float[]{i},new float[]{2*i+8});
        }
    }

    private static void trainData( int epoch, float learningRate){
        for(int i=0; i<epoch; i++){
            for(int j=0; j<dataSet.length; j++){
                forwardProp(dataSet[j].inputData);
                backwardProp(learningRate, dataSet[j]);
            }
        }
    }

    //Sum of all gradients connected to a given neuron in a layer
    public static float sumGradient(int numIndex , int layerIndex ){
        float sum=0;
        Layer current= layer[layerIndex];
        for(int i=0; i<current.neuron.length; i++){
            Neuron n= current.neuron[i];
            sum+= n.gradient*n.weights[numIndex];
        }
        return sum;
    }


}

