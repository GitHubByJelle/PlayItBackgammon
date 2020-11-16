package AI.TDGammon;

public class NeuralNet {

    static Layer[] layer;
    static TrainData[] dataSet;

    public static void main(String[] args) {
        Neuron.range(0, 1);
        layer= new Layer[3];
        layer[0]= null;
        layer[1]= new Layer(2,5);
        layer[2]= new Layer(5,1);
        createTrainingData();
    }

    public static void backwardProp(float learningRate, TrainData data){
        int numLayers= layer.length;
        int outLayer= numLayers-1;

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
                for(int k=0; k<layer[i-1].neuron[j].weights.length; k++){
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

    }

    //Sum of all gradients connected to a given neuron in a layer
    public static float sumGradient(int leftIndex , int rightIndex ){
        float sum=0;
        Layer current= layer[leftIndex];
    }


}

