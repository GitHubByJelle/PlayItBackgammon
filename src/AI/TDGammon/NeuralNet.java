package AI.TDGammon;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import AI.RandomBot;
import World.Board;
import World.Player;

public class NeuralNet implements Serializable {
    private ArrayList<TrainData> dataSet;
    private Layer[] layer;
    private float lr;


    NeuralNet(float lr){
        Neuron.range(-1, 1);
        //this.dataSet= dataSet;
        this.lr = lr;

        //Network for simple functions
//        this.layer= new Layer[3];
//
//        this.layer[0]= null;
//        this.layer[1]= new Layer(2,2);
//        this.layer[2]= new Layer(2,1);


        //small network for TD-gammon
        layer= new Layer[3];
        //layer[0]= null;
        float[] input = new float[4*27*2];
        layer[0] = new Layer(input);
        layer[1]= new Layer(4*27*2,40);
        layer[2]= new Layer(40,1);


        //Big network for TD-gammon
        // Using 25 Layers
        /*
        layer = new Layer[25];
        layer[0]= null;
        layer[1]= new Layer(dataSet.get(0).getData().length,4*27*2*2);
        //System.out.println("Leftie: " + (4*27*2) + " Rightie: " + (4*27*2*2));
        int in;
        for (in = 2; in < layer.length-1; in++){
            layer[in] = new Layer(4*27*2*2 - (in-2) * 16, 4*27*2*2 - (in-1) * 16);
            //System.out.println(i + "Leftie: " + (4*27*2*2 - (i-2) * 16) + " Rightie: " + (4*27*2*2 - (i-1) * 16));
        }
        layer[24] = new Layer(4*27*2*2 - (in-2) * 16,dataSet.get(0).getExpectedOutput().length);

        System.out.println(dataSet.size());
         */
    }

    public void forwardProp(float[] input) {

        layer[0] = new Layer(input);

        for (int i = 1; i < layer.length; i++) {
            for (int j = 0; j < layer[i].neuron.length; j++) {
                float sum = 0;
                for (int k = 0; k < layer[i - 1].neuron.length; k++) {
                    sum += (layer[i - 1].neuron[k].value) * (layer[i].neuron[j].weights[k]);
                }
                layer[i].neuron[j].value = MathUtils.sigmoid(sum);
            }
        }
    }

    float[] returnOutput(float[] input){
        int numLayers= layer.length;
        int outLayer= numLayers-1; //index of the last layer
        float[] ans= new float[layer[outLayer].neuron.length];

        forwardProp(input);
        for(int i=0; i<layer[outLayer].neuron.length; i++){
            ans[i]=layer[outLayer].neuron[i].value;
        }
        return ans;
    }

    public Layer[] getLayer() {
        return layer;
    }

    public void setLayer(Layer[] layer) {
        this.layer = layer;
    }

    public static void PlayMultipleTimes(Player.Bot one, Player.Bot two, Board b, int NumberOfGames){
        for(int i = 0; i<NumberOfGames; i++){
            //b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            PlayWithRandomDie(b);
//            System.out.println(b);
        }
    }

     static void PlayWithRandomDie(Board b){
        b.getDie().getDieList().clear();
        b.getDie().generateDie();
        b.getDie().getNextRoll();
        while(!b.checkWinCondition()){
//            System.out.println(b);
            b.getGameLoop().process();
//            System.out.println(Arrays.toString(b.getDie().getCurRoll()));
        }
    }

     static float [] giveReward(int[][] bRep, int id){
//        float [] win= new float[4];//{player 0 win, player 0 gammon, player 1 win, player 1 gammon}
//        if(b.getPlayer1().getPiecesOutOfPlay()==15){
//            if(b.getPlayer2().getPiecesOutOfPlay()==0){
//                win[1]=1f;
//            }else{
//                win[0]=1f;
//            }
//        } else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
//            if(b.getPlayer1().getPiecesOutOfPlay()==0){
//                win[3]=1f;
//            }else{
//                win[2]=1f;
//            }
//        }

         float[] win = new float[1];
         if(id==0){
             if(bRep[26][1]==15){
                 win[0] = 1;
             }else{
                 win[0] = 0;
             }
         }else{
             if(bRep[26][2]==15){
                 win[0] = 1;
             }else{
                 win[0] = 0;
             }

         }

         return win;
    }

    static float[] giveReward(Board b){

        float[] win = new float[1];
        if(b.getPlayer1().getPiecesOutOfPlay()==15)
            win[0] = 1;
        else
            win[0] = 0;
        return win;

    }

    @Override
    public String toString(){
        String res="";
        res+="learningrate="+lr+";\n";
//        res+="dataSet=";
//        for(int i=0;i<dataSet.size();i++){
//            res+=dataSet.get(i).toString();
//        }
       // res+="layer=";
        for(int i=0;i<layer.length;i++){
            res+="layer="+layer[i].toString()+"\n";
        }
        return res;
    }

    NeuralNet(float lr, Layer[]layer){
        this.lr=lr;
        this.layer=layer;
    }

    @Override
    public boolean equals(Object other){
        return toString().equals(other.toString());
    }

}


