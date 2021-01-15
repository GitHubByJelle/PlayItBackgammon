package AI.TDGammon;
import java.io.*;


public class ImportNN {
    public static NeuralNet importNet(String network) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(network));
        NeuralNet net = (NeuralNet) ois.readObject();
        ois.close();

        return net;
    }
}

