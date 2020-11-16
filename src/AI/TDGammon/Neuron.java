package AI.TDGammon;

public class Neuron {

    static float minWeight;
    static float maxWeight;

    float[] weights;
    float[] weightsCache;
    float value=-1;
    float gradient;
    float bias;

    //Output + hidden Neurons
    public Neuron (float bias, float[] weights){
        this.weights= weights;
        this.bias=bias;
        this.weightsCache=this.weights;
        this.gradient=0;
    }

    //Input Neurons
    public Neuron(float val){
        this.weights= null;
        this.bias=-1;
        this.weightsCache= null;
        this.gradient= -1;
        this.value=val;

    }

    public static void range(int min, int max){
        minWeight=min;
        maxWeight=max;
    }


    public void update_weight(){
        this.weights=this.weightsCache;
    }
}
