package AI;

import World.Board;
import World.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class BotTestingGround {
    private static ArrayList<int[]> i= new ArrayList<int[]>();
    public static int counter = 0;
    static double[] weightsarr = {0.5572496840622174, 0.6564290279969202, 0.728965249865199, 0.6425858985646018, 0.5246025450781864};
    static Board b;
    static double[] diffweightsarr = {1,1,1,1,1};

    public static void main(String []args){
        b= new Board();
//         Player.Bot one= new SimpleBot(0);
//         Player.Bot two = new SimpleBot(1);
//        int[][] l ={{1,2},{1,2} , {2,4} , {3,6} , {4,5}  ,{4,5} , {1,3} , {5,5},  {1,5},  {1,6}, {3,3} , {4,6} , {4,3},  {4,4} , {3,4},  {4,3},  {1,1} , {2,1} , {1,4},  {5,3}, {4,6} , {3,5} , {6,4},  {4,2} , {1,3} , {5,1} , {6,2},  {2,1},  {3,1},  {5,2}, {3,4} , {2,2} , {2,1},  {3,1} , {2,5} , {6,1} , {2,5} , {2,4},  {4,5} , {5,3}, {3,5} , {1,3} , {4,6},  {1,5},  {5,4} , {6,5},  {5,5} , {2,4},  {3,3} , {3,2}, {6,3},  {2,4} , {3,1} , {4,3} , {5,3} , {6,4} , {5,5} , {5,2} , {3,3},  {6,3}, {3,3} , {3,5},  {5,3} , {3,5} , {3,2} , {3,6} , {5,4} , {2,3},  {1,3} , {6,1}, {1,4} , {5,6},  {2,5} , {4,6},  {5,5} , {3,3} , {3,3} , {2,1} , {5,6},  {4,3}, {3,1}  ,{5,3}  ,{5,2} , {6,3} , {4,5} , {5,2},  {3,3}  ,{6,6} , {4,5} , {2,2}, {4,2}  ,{5,3},  {5,1} , {2,4} , {2,6} , {1,3},  {2,6} , {5,4},  {3,5} , {3,5}, {6,2} , {3,6},  {5,3} , {6,3} , {1,3},  {6,4},  {3,1} , {4,5} , {5,1} , {1,5}, {1,3}  ,{4,1},  {6,4} , {6,3} , {5,2} , {5,4} , {4,5},  {2,5} , {6,6} , {2,6}, {5,1} , {2,2},  {5,6},  {1,4},{2,2} , {1,3} , {2,2}  ,{5,6}  ,{5,4} , {4,4}, {5,5} , {1,4} , {5,4} , {5,4}  ,{6,4} , {2,1} , {6,4} , {5,6},  {2,5} , {1,3}, {2, 5}, {1,2} , {5,3}  ,{4,5},  {6,4},  {3,3} , {3,4},  {5,2},  {6,3} , {6,1}, {3,4}};
//        for(int k=0;k<l.length;k++){
//            i.add(l[k]);
//        }
//        Player.Bot one= new PrimeBlitzingBot(0);
//        Player.Bot two = new PrimeBlitzingBot(1);
//        Player.Bot one= new BotA(0,weightsarr);
//        Player.Bot two = new BotA(1,diffweightsarr);
//        one.pausing=false;
//        two.pausing=false;
//        b.setPlayers(one,two);
//        b.createBotLoop();
//        long a = System.nanoTime();
//        testMultipleTimes(one,two);
//        System.out.println((System.nanoTime()-a)/1000000000.);
        ABbot one = new ABbot(b,0);
        ABbot two = new ABbot(b,1);
        one.setOpponent(two);
        two.setOpponent(one);
	    one.pausing = false;
	    two.pausing = false;
	    b.setPlayers(one, two);
	    b.createBotLoop();
        long a = System.nanoTime();
        System.out.println("Depth of 3: ");
        one.alpha_beta_pruning_result();
        System.out.println((System.nanoTime()-a)/1000000000.);


    }

    public static void testMultipleTimes(Player.Bot one, Player.Bot two ){
        for(int i = 0; i<100; i++){
            b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie();
//            System.out.println(b);
        }
        System.out.println(counter);
    }

    private static void testWithRandomDie(){
        b.getDie().getDieList().clear();
        b.getDie().generateDie();
        b.getDie().getNextRoll();
        while(!b.checkWinCondition()){
//            System.out.println(b);
            b.getGameLoop().process();
//            System.out.println(Arrays.toString(b.getDie().getCurRoll()));
        }

        giveWinner( b);
    }

    private static void testWithSameGivenRoll( ArrayList<int[]> i){
        b.getDie().setDieTo(i);
        while(!b.checkWinCondition()){
            b.getGameLoop().process();
            //System.out.println(b);
        }

        giveWinner(b);
    }


    private static void giveWinner(Board b){
        if(b.getPlayer1().getPiecesOutOfPlay()==15){
//            System.out.println("ONE WINS"+b.getPlayer1().getName());
            counter++;
        }else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
//            System.out.println("TWO WINS"+b.getPlayer2().getName());
        }else{
            //System.out.println("Question Epic Life decisions");
        }
    }
}
