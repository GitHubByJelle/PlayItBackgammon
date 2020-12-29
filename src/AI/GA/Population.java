package AI.GA;

import AI.GA.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population{
    //Values
    Random r = new Random();
    ArrayList<Individual> poolOfIndividuals = new ArrayList<Individual>();
    private final int POOL_SIZE = 20;
    private final double mutationrate = 0.3;
    private double similarityrate = 0.25;

    // Default Constructor
    public Population() {
        for (int i = 0; i < POOL_SIZE; i++){
            this.poolOfIndividuals.add(new Individual());
        }
    }
    // Constructor
    public Population(Individual bestgenome) {
        this.poolOfIndividuals.add(bestgenome);
        for(int i = 0; i < 0.6*POOL_SIZE; i++){
            this.poolOfIndividuals.add(new Individual(bestgenome));
        }
        while(this.poolOfIndividuals.size()< POOL_SIZE){
            this.poolOfIndividuals.add(new Individual());
        }
    }

    public void PopulationEvolver()  {
        //tournament selection
        this.setEveryIndividualEnemyWeights(this.getFittest().getGenome());
        Individual tempInd;
        for(int i = 0; i < 0.4 * POOL_SIZE; i++) {
            ArrayList<Individual> best2 = this.getRandomBest2_NoSimilarity();
            if (r.nextDouble() < mutationrate){
                tempInd = new Individual (new Individual(best2.get(0),best2.get(1)));
            } else {
                tempInd = new Individual(best2.get(0),best2.get(1));
            }
            this.poolOfIndividuals.add(tempInd);
        }
        //this.poolOfIndividuals.add(previousPopulation.getFittest());
        Collections.sort(this.poolOfIndividuals);
        for(int i = 0; i < POOL_SIZE*0.5; i++){
            this.poolOfIndividuals.remove(0);
        }
        for(Individual ind: this.getPoolOfIndividuals()){
                ind.calcFitnessAgain();
        }

        while(this.poolOfIndividuals.size() < POOL_SIZE){
            this.poolOfIndividuals.add(new Individual());
        }
    }

    public ArrayList<Individual> getPoolOfIndividuals(){
        return this.poolOfIndividuals;
    }


    public Individual getFittest(){
        double maxFit = 0;
        for (Individual maxInd : this.poolOfIndividuals){
            maxFit = Math.max(maxInd.getFitness(), maxFit);
        }
        for (Individual searchMax : this.poolOfIndividuals){
            if (maxFit == searchMax.getFitness()){
                return searchMax;
            }
        }
        return this.poolOfIndividuals.get(0);
    }
    // Selects 10 random Individuals and returns best 2


    public ArrayList<Individual> getRandomBest2_NoSimilarity(){
        ArrayList<Individual> tempList = new ArrayList<Individual>();
        for(int i = 0; i < 0.4 * POOL_SIZE; i++){
            tempList.add(this.poolOfIndividuals.get(r.nextInt(this.poolOfIndividuals.size())));
        }
        Collections.sort(tempList, Collections.reverseOrder());
        ArrayList<Individual> returnlist = new ArrayList<Individual>();
        returnlist.add(tempList.get(0));
        for(int i = 1; i < tempList.size(); i++){
            if(!(ToSimilar(tempList.get(0),tempList.get(i)))){
                returnlist.add(tempList.get(i));
                return returnlist;
            }
        }
        returnlist.add(tempList.get(1));
        return returnlist;
    }
    public ArrayList<Individual> getRandomBest2(){
        ArrayList<Individual> tempList = new ArrayList<Individual>();
        for(int i = 0; i < 0.4 * POOL_SIZE; i++){
            tempList.add(this.poolOfIndividuals.get(r.nextInt(this.poolOfIndividuals.size())));
        }
        Collections.sort(tempList);

        for (int i = 0; i < 0.4*POOL_SIZE - 2; i++){
            tempList.remove(0);
        }


            return tempList;
    }
    public boolean ToSimilar(Individual a, Individual b){
        for(int i = 0; i < 5; i++){
            if(((a.getGenome()[i] - b.getGenome()[i]) > similarityrate) && ((a.getGenome()[i] - b.getGenome()[i]) < -similarityrate)){
                return true;
            }
        }
        return false;
    }

    public double getAverage(){
        double a = 0;
        for(int i = 0; i  < this.poolOfIndividuals.size(); i++){
            a += this.poolOfIndividuals.get(i).getFitness();
        }
        return (a/this.poolOfIndividuals.size());
    }
    public void setEveryIndividualEnemyWeights(double[] weights){
        for(Individual ind : this.getPoolOfIndividuals()){
            ind.setEnemyWeightGenome(weights);
        }
    }
}
