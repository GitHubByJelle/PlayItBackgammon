package AI.TDGammon;

public class Layer {
    public Neuron[] neuron;

    //Output layer + hidden layers
    public Layer( int nbrNeuronsLeft, int nbrNeuronsRight ){
        this.neuron= new Neuron[nbrNeuronsRight];

        for(int i=0; i<nbrNeuronsRight; i++){
            float[] weights= new float[nbrNeuronsLeft];
            for (int j=0; j<nbrNeuronsLeft; j++){
                weights[j]= MathUtils.randomFloat(Neuron.minWeight, Neuron.maxWeight);
            }
            neuron[i]= new Neuron(MathUtils.randomFloat(0,1), weights);

        }
    }

    //input Layer
    public Layer(float[] input){
        this.neuron= new Neuron[input.length];
        for(int i=0; i< input.length; i++){
            this.neuron[i]= new Neuron(input[i]);
        }
    }
}
