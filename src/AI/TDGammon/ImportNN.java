package AI.TDGammon;
import java.io.*;

public class ImportNN {
    public static NeuralNet importNet(String network){
        try {

            // Find the file
            FileInputStream file_net = new FileInputStream(new File(network));

            // Create a stream
            ObjectInputStream in_net = new ObjectInputStream(file_net);

            NeuralNet loaded_net = (NeuralNet) in_net.readObject();

            // Close the steam
            in_net.close();

            // Close the file
            file_net.close();

            System.out.println(" ===> " + network + " imported");
            return loaded_net;

        } catch (FileNotFoundException e) {
            System.out.println("File with name: " + network + " not found! Recheck spelling");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error initializing stream");
        }
        System.out.println(" ! ===> NO NETWORK IMPORTED");
        return null;
    }
}

