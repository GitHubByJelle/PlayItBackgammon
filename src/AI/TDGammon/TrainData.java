package AI.TDGammon;

public class TrainData {

    float[] data;//predicted output
    float[] expectedOutput;//Real output

    public TrainData(float[] data, float[] expectedOutput){
        this.data=data;
        this.expectedOutput=expectedOutput;
    }

    public float[] getData() {
        return data;
    }

    public float[] getExpectedOutput() {
        return expectedOutput;
    }
}
