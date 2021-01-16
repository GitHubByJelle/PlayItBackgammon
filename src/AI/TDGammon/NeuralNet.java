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
    private Layer[] layer;
    private float lr;


    NeuralNet(float lr){
        Neuron.range(-1, 1);
        this.lr = lr;

        //Make a network for TDG
        layer= new Layer[3];
        layer[0] = new Layer(new float[4*27*2]);
        layer[1]= new Layer(4*27*2,40);
        layer[2]= new Layer(40,1);

    }

    public void forwardProp(float[] input) {
        // Use forward propagation
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
        // Use forward propagation to calculate the values at the last node(s) gives the input
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

    public static void PlayGame(Player.Bot one, Player.Bot two, Board b){
        // Reset everything and continue
        one.resetPlayer();
        two.resetPlayer();
        b.setPlayers(one,two);
        b.createBotLoop();
        PlayWithRandomDie(b);
    }

     static void PlayWithRandomDie(Board b){
        // Get random die
        b.getDie().getDieList().clear();
        b.getDie().generateDie();
        b.getDie().getNextRoll();
        // If no one won, continue game (process)
        while(!b.checkWinCondition()){
            b.getGameLoop().process();
        }
    }

     static float [] giveReward(int[][] bRep, int id){
        // Determine reward. Gives a 1 if player 1 wins, gives a 0 if player 2 wins
         float[] win = new float[1];
         // If player 1 (id is 0)
         if(id==0){
             // See if player 1 wins and give 1, otherwise reward 0
             if(bRep[26][1]==15){
                 win[0] = 1;
             }else{
                 win[0] = 0;
             }
         }else{
             // See if player 1 wins and give 1, otherwise reward 0
             if(bRep[26][2]==15){
                 win[0] = 1;
             }else{
                 win[0] = 0;
             }
         }

         // Return reward
         return win;
    }

    @Override
    public String toString(){
        String res="";
        res+="learningrate="+lr+";\n";
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