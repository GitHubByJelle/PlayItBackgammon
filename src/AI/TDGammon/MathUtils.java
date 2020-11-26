package AI.TDGammon;

public class MathUtils {

    // Generating a random number
    public static float randomFloat(float min, float max){
        float a= (float) Math.random();
        float num= min+ (float)Math.random()*(max-min);
        if(a<0.5)
            return num;
        else
            return -num;
    }

    //ReLu function
    public static float reLu(float x) {
        return (float) Math.max(0,x);
    }

    //Sigmoid function
    public static float sigmoid(float x){
        return (float) (1/(1+Math.pow(Math.E, -x)));
    }

    //Derivative of the sigmoid
    public static float sigmoidDerivative(float x){
        return sigmoid(x)*(1-sigmoid(x));
    }

    //Squared error (used for back-prop)
    public static float squaredError(float output, float target){
        return (float) (0.5*Math.pow(2,(target-output)));
    }

    //Overall error rate
    public static float sumSquaredError(float[] outputs, float[] targets){
        float sum=0;
        for(int i=0; i<outputs.length; i++){
            sum+= squaredError(outputs[i], targets[i]);
        }
        return sum;
    }


}
