package AI.TDGammon;

public class TrainData {

    float[] inputs;// input of the data
    float[] targets;//Real output

    public TrainData(float[] data, float[] targets){
        this.inputs =data;
        this.targets = targets;
    }

    public float[] getData() {
        return inputs;
    }

    public float[] getTarget() {
        return targets;
    }
}
