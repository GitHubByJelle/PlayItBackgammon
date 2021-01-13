package AI.TDGammon;
import java.io.*;

public class ExportNN {
        /**
         * Saves the network to a local file
         * @param target_network NeuralNet target_network object
         */
        public static void exportNetworks(NeuralNet target_network){
            try {
                // Create the files
                 FileOutputStream file_target_network = new FileOutputStream(new File("AI/TDGammon/target_network"));

                // Create a stream
                 ObjectOutputStream out_target_network = new ObjectOutputStream(file_target_network);

                // Write networks to file
                 out_target_network.writeObject(target_network);

                // Close the steam
                 out_target_network.close();

                // Close the file
                 file_target_network.close();

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }

            System.out.println(" ===> network exported");
        }
}


