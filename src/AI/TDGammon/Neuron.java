package AI.TDGammon;

public class Neuron {

    static float minWeight;
    static float maxWeight;

    float[] weights;
    float[] weightsCache;
    float value=0;
    float gradient;
    float bias;

    //Output + hidden Neurons
    public Neuron (float bias, float[] weights){
        this.weights= weights;
        this.bias=bias;
        this.weightsCache=this.weights; //to have the same size of the weights
        this.gradient=0;
    }

    //Input Neurons
    public Neuron(float val){
        this.value=val;

        this.weights= null; //not existing yet
        this.bias=-1; //to change it afterwards
        this.weightsCache= null; //not existing yet
        this.gradient= -1; //to change it afterwards

    }

    public static void range(int min, int max){
        minWeight=min;
        maxWeight=max;
    }


    public void update_weight(){
        this.weights=this.weightsCache;
    }


}
