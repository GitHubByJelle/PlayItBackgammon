package AI;

import AI.AlphaBeta.ABbot;
import AI.AlphaBeta.AlphaBetaBot;
import AI.GA.TMM;
import AI.TDGammon.TDG;
import Utils.Variables;
import World.Board;
import World.Player;

import java.util.ArrayList;
import java.util.Random;

public class BotTestingGround {
    private static ArrayList<int[]> i= new ArrayList<int[]>();
    public static int counter = 0;
    static double[] weightsarr = {0.5572496840622174, 0.6564290279969202, 0.728965249865199, 0.6425858985646018, 0.5246025450781864};
    static Board b;
    static double[] diffweightsarr = {1,1,1,1,1};

    public static void main(String []args){
        executeTourney(10);
    }

    public static void testMultipleTimes(Player.Bot one, Player.Bot two){
        for(int i = 0; i<1000; i++){
            b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie();
            //System.out.println(b);
        }
        System.out.println(counter);
    }
    public static void testMultipleTimes(Player.Bot one, Player.Bot two, int numGames){
        for(int i = 0; i<numGames; i++){
            b = new Board();

            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie();
            //System.out.println(b);
        }
        //System.out.println(one.getName() + " vs " + two.getName() + ". Number of wins: " + counter);
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

        giveWinner(b);
    }

    public static void executeOneBotTourney(int numGamesPerCombination, int secondplayer){
        Player.Bot first;
        Player.Bot second;

        for(int one = 0; one< Variables.BOTS.length; one++){
                b = new Board();
                b.setPlayers(Variables.BOTS[one],Variables.BOTS[secondplayer]);
                first=(Player.Bot)b.getPlayer1();
                second=(Player.Bot)b.getPlayer2();
                first.pausing=false;
                second.pausing=false;
                b.createBotLoop();
                testMultipleTimes(first,second,numGamesPerCombination);
                System.out.println(String.format("%17s vs %17s  First wins = %4d  Second wins = %4d", first.getName(), second.getName(), counter, (numGamesPerCombination-counter)));
                counter=0;
            if(!Variables.BOTS[one].equals(Variables.ABB)) {
                b = new Board();
                b.setPlayers(Variables.BOTS[secondplayer], Variables.BOTS[one]);
                second = (Player.Bot) b.getPlayer1();
                first = (Player.Bot) b.getPlayer2();
                first.pausing = false;
                second.pausing = false;
                b.createBotLoop();
                testMultipleTimes(second, first, numGamesPerCombination);
                System.out.println(String.format("%17s vs %17s  First wins = %4d  Second wins = %4d", second.getName(), first.getName(), counter, (numGamesPerCombination - counter)));
                counter = 0;
            }


        }
    }

    public static void executeTourney(int numGamesPerCombination){
        Player.Bot first;
        Player.Bot second;

        for(int one = 0; one< Variables.BOTS.length; one++){
            for(int two=0;two<Variables.BOTS.length;two++){

                    b = new Board();
                    b.setPlayers(Variables.BOTS[one], Variables.BOTS[two]);
                    first = (Player.Bot) b.getPlayer1();
                    second = (Player.Bot) b.getPlayer2();
                    first.pausing = false;
                    second.pausing = false;
                    b.createBotLoop();
                    testMultipleTimes(first, second, numGamesPerCombination);
                    System.out.println(String.format("%17s vs %17s  First wins = %4d  Second wins = %4d", first.getName(), second.getName(), counter, (numGamesPerCombination - counter)));
                    counter = 0;

            }
        }

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
    public static void testAgainstAlphaBeTaMultipleTimes(Player.Bot one,Player.Bot two){
        for(int i = 0; i<100; i++){
            b = new Board();
            one = new AlphaBetaBot(0);
            one.setOpponent(two);
            one.opponent.setBoard(b);
            one.pausing = false;
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();
            testWithRandomDie();
            //System.out.println(b);
            if(i % 10 == 0){
                System.out.println("Counting wins: " + counter + " over " + i);
            }
        }
        System.out.println(counter);

    }
}
