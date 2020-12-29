package AI.GA;


import World.Board;
import World.Player;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Individual implements Comparable<Individual>{
    //values
    Random r = new Random();
    private final int GENOME_LENGTH = 5;
    private double[] weightGenome = new double[GENOME_LENGTH];
    private double[] enemyWeightGenome = new double[GENOME_LENGTH];
    private double d;

    private double fitness;
    private final int SIZE_OF_INDIVIDUAL = 100;
    //default constructor for the beginning
    public Individual(){
        for (int i = 0; i < GENOME_LENGTH; i++){
            this.weightGenome[i] = r.nextDouble();
        }
        for (int i = 0; i < GENOME_LENGTH; i++){
            this.enemyWeightGenome[i] = r.nextDouble();
        }
        this.fitness = individualAvg();
    }


    public static void main(String[] args) {
        Individual a = new Individual();
        System.out.println(a.getFitness());
    }

    //constructor from starting genome
    public Individual(double[] setWeightGenome){
        for (int i = 0; i < GENOME_LENGTH; i++) {
            this.weightGenome[i] = setWeightGenome[i];
        }
        this.fitness = individualAvg();

    }

    // Crossoverchild 2 best entries
    public Individual(Individual bestGenome1, Individual bestGenome2){
        for(int i = 0; i < GENOME_LENGTH; i++){
            this.weightGenome[i] = (bestGenome1.getGenome()[i] * bestGenome1.getFitness() + bestGenome2.getGenome()[i] * bestGenome2.getFitness())/(bestGenome1.getFitness()+bestGenome2.getFitness());
        }
        this.setEnemyWeightGenome(bestGenome1.getEnemyWeightGenome());
        this.fitness = individualAvg();
    }

    // Mutation
    public Individual(Individual randomGenome){
        this.weightGenome = randomGenome.getGenome();
        int randomint = r.nextInt(4);
        double randomdouble = 1.2 - (r.nextDouble() * 0.4);
        this.weightGenome[randomint] = this.weightGenome[randomint] * randomdouble;
        this.setEnemyWeightGenome(randomGenome.getEnemyWeightGenome());

        this.fitness = individualAvg();
        //System.out.println(this.fitness);

    }
    public double[] getGenome(){
        return this.weightGenome;
    }
    public double getFitness(){
        return this.fitness;
    }
    public void setEnemyWeightGenome(double[] arr){
        for(int i = 0; i<arr.length; i++){
            this.enemyWeightGenome[i] = arr[i];
        }
    }
    public void calcFitnessAgain() {
        this.fitness = individualAvg();
    }
    public double[] getEnemyWeightGenome(){return this.enemyWeightGenome;}
//    public double individualAvg(){
//        this.d = 0;
//        Board b;
//        Player.Bot one= new BotA(0, this.weightGenome);
//        Player.Bot two = new BotA(1, this.enemyWeightGenome);
//        for(int i = 0; i<SIZE_OF_INDIVIDUAL; i++){
//            one.pausing=false;
//            two.pausing=false;
//            b = new Board();
//            one.resetPlayer();
//            two.resetPlayer();
//            b.setPlayers(one,two);
//            b.createBotLoop();
//
//            b.getDie().getDieList().clear();
//            b.getDie().generateDie();
//            b.getDie().getNextRoll();
//            while(!b.checkWinCondition()){
//                b.getGameLoop().process();
//            }
//            if(b.getPlayer1().getPiecesOutOfPlay()==15){
//                this.d++;
//            }else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
//            }else{
//                System.out.println("Question Epic Life decisions");
//            }
//
//
//        }
//        return this.d/SIZE_OF_INDIVIDUAL;
//    }
public double individualAvg() {
    Runnable task = () -> {
        try{
            Board b;
            Player.Bot one= new TMM(0, this.weightGenome);
            Player.Bot two = new TMM(1, this.enemyWeightGenome);
            one.pausing=false;
            two.pausing=false;
            b = new Board();
            one.resetPlayer();
            two.resetPlayer();
            b.setPlayers(one,two);
            b.createBotLoop();

            b.getDie().getDieList().clear();
            b.getDie().generateDie();
            b.getDie().getNextRoll();
            while(!b.checkWinCondition()){
                b.getGameLoop().process();
            }
            if(b.getPlayer1().getPiecesOutOfPlay()==15){
                synchronized ("string") {
                    this.d++;
//                    System.out.println("What the Game sees: " + this.d);

                }
            }else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
            }else{
                System.out.println("Question Epic Life decisions");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    };
    this.d = 0;
    ExecutorService ex = Executors.newFixedThreadPool(5);

    IntStream.range(0,SIZE_OF_INDIVIDUAL).forEach(item -> ex.execute(task));
    ex.shutdown();
    try {
        ex.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
//    System.out.println("What the Ind sees: " + a);
    return this.d/SIZE_OF_INDIVIDUAL;
}


    @Override
    public int compareTo(Individual o) {
        int comp = 0;
        if (fitness < o.getFitness()){
            comp = -1;
        }
        else if (fitness > o.getFitness()){
            comp = 1;
        }
        return comp;
    }
    
}
