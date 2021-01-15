package AI.TDGammon;
import java.io.*;

public class ExportNN {
        /**
         * Saves the network to a local file
         * @param target_network NeuralNet target_network object
         */
        public static void exportNetwork(NeuralNet target_network,String fileName) throws IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(target_network);
            oos.flush();
            oos.close();
        }
}


