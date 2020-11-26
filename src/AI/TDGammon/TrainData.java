package AI.TDGammon;

public class TrainData {

    float[] inputData;// input of the data
    float[] expectedOutput;//Real output

    public TrainData(float[] data, float[] expectedOutput){
        this.inputData=data;
        this.expectedOutput=expectedOutput;
    }

    public float[] getData() {
        return inputData;
    }

    public float[] getExpectedOutput() {
        return expectedOutput;
    }
}
