package AI.TDGammon;

public class TrainData {

    float[] data;
    float[] expectedOutput;

    public TrainData(float[] data, float[] expectedOutput){
        this.data=data;
        this.expectedOutput=expectedOutput;
    }
}
