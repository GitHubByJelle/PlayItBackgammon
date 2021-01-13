package AI.TDGammon;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NNFile {
    public static String default_NN_path = "src//AI//TDGammon//SavedNN//";

    public static void export(NeuralNet n, String name){
        String str= nnToString(n);
        File file= new File(default_NN_path+name);

        PrintWriter writer;
        try {

            file.delete();
            file.createNewFile();
            writer = new PrintWriter(new FileWriter(file));
            writer.println(str);
            writer.close();

        } catch (IOException e) {
            System.err.println("Given file ("+file+") is invalid");
            e.printStackTrace();
        }

    }

    private static String nnToString(NeuralNet n) {

        return n.toString();
    }

    static String [] tags = new String[]{"learningrate=","layer=","weights=",
            "weightsCache=","value=","gradient=", "bias="};//"inputs=","targets=","dataSet=" };

    public static NeuralNet importNN(String fileName){

        String[] elements=new String[]{};
        try {
            BufferedReader reader = new BufferedReader(new FileReader(default_NN_path+fileName));
            String result = "";
            while (reader.ready()) {
                result += reader.readLine()+"\n";
            }
            elements= result.split(";");//seperate layers
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }


        float lr=0;
        ArrayList<Layer> layers=new ArrayList<>();
        //float[] inputs,outputs=null;
        ArrayList<Neuron> neurons=new ArrayList<>();
        float[] weights=null;
        float[] weightsCache=null;
        float value=0f;
        float gradient=0f;
        float bias=0f;
        String cur;
        String[] alt;
        String[] dummy1;
        String[] dummy2;
        String dummy;
        for(int i=0;i<elements.length;i++){
            cur=elements[i];
         //   System.out.println(i+" "+cur+" "+elements.length);
            if(cur.contains(tags[0])){
                lr= Float.parseFloat(cur.substring(tags[0].length()));
            }else if(cur.contains(tags[1])){//layer
                cur= cur.substring(tags[1].length()+1);
                alt= cur.split(":");//split different neurons
                for(int a=0;a<alt.length;a++){
                    dummy1 = alt[a].split("\n");//split different lines ie different things in the neuron
                    for(int d=0;d<dummy1.length;d++) {
                        if (dummy1[d].contains(tags[2])) {//weights
                            if (!dummy1[d].substring(tags[2].length()).equals("null")) {
                                dummy = dummy1[d].substring(tags[2].length() + 1, dummy1[d].length() - 1);//float array as string
                                dummy2 = dummy.split(",");
                                weights = new float[dummy2.length];
                                for (int w = 0; w < dummy2.length; w++) {
                                    weights[w] = Float.parseFloat(dummy2[w]);
                                }
                            }
                        } else if (dummy1[d].contains(tags[3])) {//weights cache
                            if (!dummy1[d].substring(tags[3].length()).equals("null")) {
                                dummy = dummy1[d].substring(tags[3].length() + 1, dummy1[d].length() - 1);//float array as string
                                dummy2 = dummy.split(",");
                                weightsCache = new float[dummy2.length];
                                for (int w = 0; w < dummy2.length; w++) {
                                    weightsCache[w] = Float.parseFloat(dummy2[w]);
                                }
                            }
                        } else if (dummy1[d].contains(tags[4])) {//value
                            value = Float.parseFloat(dummy1[d].substring(tags[4].length()));

                        } else if (dummy1[d].contains(tags[5])) {//gradient
                            gradient = Float.parseFloat(dummy1[d].substring(tags[5].length()));

                        } else if (dummy1[d].contains(tags[6])) {//bias
                            bias = Float.parseFloat(dummy1[d].substring(tags[6].length()));
                        }


                    }
                    neurons.add(new Neuron(weights, weightsCache, value, gradient, bias));
                    weights=null;
                    weightsCache=null;
                    value=0f;
                    gradient=0f;
                    bias=0f;
                }


                layers.add(new Layer(neurons.toArray(new Neuron[neurons.size()])));
                neurons.clear();
            }
        }


        NeuralNet n= new NeuralNet(lr, layers.toArray(new Layer[layers.size()]));

        return n;
    }
}
