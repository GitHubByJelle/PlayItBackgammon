package AI;

import World.Board;
import World.Player;

import java.util.ArrayList;

public class BotTestingGround {
    private static ArrayList<int[]> i= new ArrayList<int[]>();
    public static int counter = 0;
    public static void main(String []args){
        Board b= new Board();
        double[] weightsarr = {0.6106505499809142, 0.3721526733358448, 0.5524562882989518, 0.23491350586200554, 0.42100869894189397};

        double[] diffweightsarr = {1,1,1,1,1};
        Player.Bot one= new BotA(0, weightsarr);
        Player.Bot two = new BotA(1, diffweightsarr);
//        int[][] l ={{1,2},{1,2} , {2,4} , {3,6} , {4,5}  ,{4,5} , {1,3} , {5,5},  {1,5},  {1,6}, {3,3} , {4,6} , {4,3},  {4,4} , {3,4},  {4,3},  {1,1} , {2,1} , {1,4},  {5,3}, {4,6} , {3,5} , {6,4},  {4,2} , {1,3} , {5,1} , {6,2},  {2,1},  {3,1},  {5,2}, {3,4} , {2,2} , {2,1},  {3,1} , {2,5} , {6,1} , {2,5} , {2,4},  {4,5} , {5,3}, {3,5} , {1,3} , {4,6},  {1,5},  {5,4} , {6,5},  {5,5} , {2,4},  {3,3} , {3,2}, {6,3},  {2,4} , {3,1} , {4,3} , {5,3} , {6,4} , {5,5} , {5,2} , {3,3},  {6,3}, {3,3} , {3,5},  {5,3} , {3,5} , {3,2} , {3,6} , {5,4} , {2,3},  {1,3} , {6,1}, {1,4} , {5,6},  {2,5} , {4,6},  {5,5} , {3,3} , {3,3} , {2,1} , {5,6},  {4,3}, {3,1}  ,{5,3}  ,{5,2} , {6,3} , {4,5} , {5,2},  {3,3}  ,{6,6} , {4,5} , {2,2}, {4,2}  ,{5,3},  {5,1} , {2,4} , {2,6} , {1,3},  {2,6} , {5,4},  {3,5} , {3,5}, {6,2} , {3,6},  {5,3} , {6,3} , {1,3},  {6,4},  {3,1} , {4,5} , {5,1} , {1,5}, {1,3}  ,{4,1},  {6,4} , {6,3} , {5,2} , {5,4} , {4,5},  {2,5} , {6,6} , {2,6}, {5,1} , {2,2},  {5,6},  {1,4},{2,2} , {1,3} , {2,2}  ,{5,6}  ,{5,4} , {4,4}, {5,5} , {1,4} , {5,4} , {5,4}  ,{6,4} , {2,1} , {6,4} , {5,6},  {2,5} , {1,3}, {2, 5}, {1,2} , {5,3}  ,{4,5},  {6,4},  {3,3} , {3,4},  {5,2},  {6,3} , {6,1}, {3,4}};
//        for(int k=0;k<l.length;k++){
//            i.add(l[k]);
//        }
        one.pausing=false;
        two.pausing=false;
        for(int i = 0; i<1000; i++){
            b = new Board();
            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie(b);
        }
        System.out.println(counter);
//        testWithSameGivenRoll(b, i);

    }

    private static void testWithRandomDie(Board b){
        b.getDie().getDieList().clear();
        b.getDie().generateDie();
        b.getDie().getNextRoll();
        while(!b.checkWinCondition()){
            b.getGameLoop().process();
//            System.out.println(b);
        }

        giveWinner( b);
    }

    private static void testWithSameGivenRoll(Board b, ArrayList<int[]> i){
        b.getDie().setDieTo(i);
        while(!b.checkWinCondition()){
            b.getGameLoop().process();
//            System.out.println(b);
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
            System.out.println("Question Epic Life decisions");
        }
    }
}
