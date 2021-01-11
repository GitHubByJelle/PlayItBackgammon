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
        return Math.max(0,x);
    }

    //Sigmoid function
    public static float sigmoid(float x){
        return (float) (1/(1+Math.pow(Math.E, -x)));
    }

    /**
     * @param x value we want to derive
     * @return the derivative of the sigmoid function
     */
    public static float sigmoidDerivative(float x){
        return x*(1-x);
    }

    /**
     * @param y value we want to derive
     * @return teh derivative of the relu function
     */
    public static float reluDerivative(float y) {
        if (y >= 0)
            return 1;
        else
            return 0;
    }
    //Squared error (used for back-prop)
    public static float squaredError(float output, float target){
        return (float) (Math.pow((target-output),2));
    }

    public static float binaryCrossEntropy(float output, float target){
        return (float) (target*Math.log(output) + (1-target)*Math.log(1-output));
    }

    public static float crossEntropyLoss(float output, float target){
        return (float) (target*Math.log(output) + (1-target)*Math.log(1-output));
    }
    public static float crossEntropyLossDer(float output, float target){
        return (float) (-target/output + (1-target)/(1-output));
    }

    //Squared error (used for back-prop)
    public static float differenceError(float output, float target){
        return (output-target);
    }


    public static float dot(float [] a, float []b){
        float res=0;

        for (int i = 0; i < a.length; i++)
            res += a[i] * b[i];
        return res;
    }

}
