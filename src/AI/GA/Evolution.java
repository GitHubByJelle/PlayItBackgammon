package AI.GA;

import java.util.Arrays;
import java.util.Random;

public class Evolution {
    public static void main(String[] args) throws InterruptedException {
        // Starts evolution
        int n = 0;
        Random r = new Random();
        //used to start population from genome
        //use by insert "new Individual(a)" inside the first population brackets
        double[] a = {1.5, 0.01, 0.5, 0.5, 0.2,2,1.5};
        Population population = new Population(new Individual(a));
        System.out.println("This is Generation " + n++  +  " :");
        System.out.println("With Weights array:" );
        System.out.println(Arrays.toString(population.getFittest().getGenome()));
        System.out.println(population.getFittest().getFitness());
        System.out.println("With EnemyWeights array:" );
        System.out.println(Arrays.toString(population.getFittest().getEnemyWeightGenome()));
        System.out.println();
        while(true){
            population.PopulationEvolver();
            System.out.println("This is Generation " + n++  +  " :");
            System.out.println("With Weights array:" );
            System.out.println(Arrays.toString(population.getFittest().getGenome()));
            System.out.println(population.getFittest().getFitness());
            System.out.println("With EnemyWeights array:" );
            System.out.println(Arrays.toString(population.getFittest().getEnemyWeightGenome()));
            System.out.println();

        }


    }
    //Recursive implementation of Evolution, while loop proved easier to read
    /*
    public static void EvoAlgo(Population previouspop, Population nextpop, int n) throws InterruptedException{
        if (n<5) {
            nextpop = new Population(previouspop, 0.25, true);
            System.out.println(Arrays.toString(nextpop.getFittest().getGenome()));
            n++;
            EvoAlgo(nextpop, previouspop, n);
        } else {
            return;
        }
    }

     */
}
