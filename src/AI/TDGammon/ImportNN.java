package AI.TDGammon;
import java.io.*;


public class ImportNN {
    public static String default_NN_path = "src//AI//TDGammon//SavedNN//";
    public static NeuralNet importNet(String network) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(default_NN_path+network));
        NeuralNet net = (NeuralNet) ois.readObject();
        ois.close();

        return net;
    }
}

