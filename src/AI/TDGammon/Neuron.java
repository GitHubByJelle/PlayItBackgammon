package AI.TDGammon;

import java.util.Arrays;

public class Neuron {
    //only there to set the bounds of the weights
    static float minWeight;
    static float maxWeight;

    //Parameters of a neuron
    public float[] weights;
    float[] weightsCache; //to store the updated weights without changing the previous ones (only useful for the backward propagation
    float value=0; //Value of the neuron initialized to 0 at the beginning
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
        this.bias=0.35f; //to change it afterwards
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



    @Override
    public String toString(){
        return "weights="+ Arrays.toString(weights)+
                "\nweightsCache=" + Arrays.toString(weightsCache)+
                "\nvalue="+value+
                "\ngradient="+gradient+
                "\nbias="+bias+"\n:";
    }
    Neuron(float[] weights,float[] weightsCache,float value, float gradient, float bias){
        this.weights=weights;
        this.weightsCache=weightsCache;
        this.value= value;
        this.gradient=gradient;
        this.bias=bias;
    }


}
