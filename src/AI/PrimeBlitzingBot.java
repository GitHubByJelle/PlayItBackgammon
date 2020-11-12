package AI;

import World.Piece;
import World.Player;
import World.Space;

import javax.swing.*;
import java.util.ArrayList;

public class PrimeBlitzingBot extends Player.Bot{
    int primeMovesCounter=0;
    int blitzMoveCounter=0;
    int turnsPassedCounter=0;
    int otherMovesCounter=0;
    private boolean blitz=true;
    private boolean prime= true;
    private double prob=0;


    public PrimeBlitzingBot(int id) {
        super(id);
    }

    public int getPrimeMovesCounter(){return primeMovesCounter;}
    public int getBlitzMoveCounter(){return blitzMoveCounter;}

    public PrimeBlitzingBot(int id,boolean blitzing, boolean prime){
        super(id);
        this.blitz=blitzing;
        this.prime=prime;
    }
    public PrimeBlitzingBot(int id, double prob){
        super(id);
        this.prob=prob;
    }

    @Override
    public String getName() {
        return "PrimeBlitzingBot";
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        moveChoice();
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice(){
        //get the possible moves we can make
        ArrayList<Space> possFrom =getPossibleFrom();
        ArrayList<Space[]> possibleMoves = getPossibleMoves(possFrom);
        Space[] bestMove = new Space[2];//move selected
        if(possFrom.contains(B.getSpaces()[B.getGameLoop().getSlainSpace()])) {//force move piece out of slain space
            for(int i=0;i< possibleMoves.size();i++)
                if(possibleMoves.get(i)[0].getId()==B.getGameLoop().getSlainSpace()) {
                    System.out.println("Piece revived");
                    bestMove[0]=possibleMoves.get(i)[0];
                    bestMove[1]=possibleMoves.get(i)[1];
                    ++otherMovesCounter;
                    break;
                }
        }else{
            Space[] bestPrimingMove= new Space[2];
            Space[] bestBlitzingMove= new Space[2];
            //make a priming move(Alaa's)
            if(prime)
                bestPrimingMove = choosePrimingMove(possibleMoves);
            //make Blitzing move(Adaia's)
            if(blitz)
                bestBlitzingMove = chooseBlitzingMove(possibleMoves); //try to return a Space[] of size 2 [from,to]




            //here would be where we decide whether to use B move or P move
            if (prime && !moveIsEmpty(bestPrimingMove)) {
                System.out.println("primingMove selected"+ bestPrimingMove[0].getId()+" "+bestPrimingMove[1].getId());
                bestMove = bestPrimingMove;
                ++primeMovesCounter;
            } else if (blitz && !moveIsEmpty(bestBlitzingMove)&& OtherPlayerHasSlainPieces()) {
                System.out.println("blitzingMove selected"+ bestBlitzingMove[0].getId()+" "+bestBlitzingMove[1].getId());
                bestMove = bestBlitzingMove;
                ++blitzMoveCounter;
            } else {
                int index = 0;
                if (possibleMoves.size() == 0) {
                    requestPassTurn();
                    ++turnsPassedCounter;
                }else {
                    while (!B.playerMove(possibleMoves.get(index)[0].getId(), possibleMoves.get(index)[1].getId())) {
                        ++index;
                        if (index > possibleMoves.size() - 1) {
                            requestPassTurn();
                            ++turnsPassedCounter;
                            return;
                        }

                    }
                    ++otherMovesCounter;
                }
                return;
            }
        }
        if(!moveIsEmpty(bestMove))
            B.playerMove(bestMove[0].getId(),bestMove[1].getId());
        else {
            requestPassTurn();
            ++turnsPassedCounter;
        }
    }

    private boolean OtherPlayerHasSlainPieces() {
        double r= Math.random();
        return r>prob;
    }

    //returns whether any part of the move ==null(ie it was never set to a value
    private boolean moveIsEmpty(Space[] m) {
        return m[0]==null || m[1]==null;
    }


//______________________________________________________________________________________________________________________
    private Space[] choosePrimingMove(ArrayList<Space[]> possibleMoves){
        int wallSize=2;
        int numWalls=3;
        Space[] res= new Space[2];//array of a [from,to] space
        int [][] currentHomeSpace= currentHome();
        int currentNumWalls = countWalls(currentHomeSpace, wallSize);
        if(currentNumWalls<numWalls) {
            res = evaluatePossPrimingMoves(currentHomeSpace, numWalls, possibleMoves, wallSize);
        }
        return res;
    }

    private Space[] evaluatePossPrimingMoves(int[][] currentHomeSpace, int currentNumWalls, ArrayList<Space[]> possibleMoves, int wallSize) {
        Space[] res=new Space[2];
        int newNumWall =0;
        //go thru the possible moves,
            //see if any of them would change the number of pieces in home spaces
            //need to decide on what to do with half walls-discourage
            //choose move that makes the most addition of walls(and half walls(?))
            //return that move
        for(int i=0;i<possibleMoves.size();i++){
            simulateMove(currentHomeSpace,possibleMoves.get(i));
            newNumWall=0;
            if(id==0) {
                //if the TO space is in home
                //if the FROM space is in Home
                if (possibleMoves.get(i)[1].getId()>18 || possibleMoves.get(i)[0].getId()>18){
                    newNumWall=countWalls(currentHomeSpace,wallSize);
                }
            }else{
                //same but for other side of board
                if (possibleMoves.get(i)[1].getId()<7 ||possibleMoves.get(i)[0].getId()<7){
                    newNumWall=countWalls(currentHomeSpace,wallSize);
                }
            }
            if(newNumWall>=currentNumWalls){
                return possibleMoves.get(i);
            }
            unDoMoveSim(currentHomeSpace,possibleMoves.get(i));
        }
        return res; //You don't assign any value to res in this method
    }

    private void simulateMove(int[][] home, Space[] move) {
        for(int i=0;i<home.length;i++){
            if(home[i][0]==move[0].getId()){
                //FROM space is in home
                --home[i][1];
            }else if(home[i][0]==move[1].getId()){
                //TO space is in home
                ++home[i][1];
            }
        }
    }

    private void unDoMoveSim(int[][] home, Space[] move) {
        for(int i=0;i<home.length;i++){
            if(home[i][0]==move[0].getId()){
                //FROM space is in home
                ++home[i][1];
            }else if(home[i][0]==move[1].getId()){
                //TO space is in home
                --home[i][1];
            }
        }
    }

    private int countWalls(int[][] currentHomeSpace, int wallSize) {
        int numWalls=0;
        for(int i=0;i<currentHomeSpace.length;i++){
            if(currentHomeSpace[i][1]>=wallSize){
                ++numWalls;
            }
        }
        return numWalls;
    }

    private int[][] currentHome(){
        int[][] homeSpaces= new int[6][2];//per space, [id,number of this player's pieces] {{6,5},{5,0},{4,0}{3,0},{2,0},{1,2}}
        int spaceIndex=0;
        if(id==0){
            for(int i=19;i<=24;i++){
                homeSpaces[spaceIndex]= new int[]{i, 0};
                if(!B.getSpaces()[i].isEmpty() && B.getSpaces()[i].getPieces().get(0).getId()==id){
                    homeSpaces[spaceIndex][1]=B.getSpaces()[i].getSize();
                }
                ++spaceIndex;
            }
        }else{
            for(int i=6;i>=1;i--){
                homeSpaces[spaceIndex]= new int[]{i, 0};
                if(!B.getSpaces()[i].isEmpty() && B.getSpaces()[i].getPieces().get(0).getId()==id){
                    homeSpaces[spaceIndex][1]=B.getSpaces()[i].getSize();
                }
                ++spaceIndex;
            }
        }
        return homeSpaces;
    }


//_________________________________________________________________________________________________________________________
    private Space[] chooseBlitzingMove(ArrayList<Space[]> possibleMoves){
        Space[] res= new Space[2];
        for (int i=0; i<possibleMoves.size(); i++){//1->24
            if(possibleMoves.get(i)[1].isEmpty() || possibleMoves.get(i)[1].getPieces().size()>1 ){//to space is empty or more pieces than we can eat
                i++;
            }
            else {
                if (id == 0) {//first player: last possible eat move
                    if (possibleMoves.get(i)[1].getPieces().get(0).getId()==1){//to space is occupied by other player
                        res[0]=possibleMoves.get(i)[0];
                        res[1]=possibleMoves.get(i)[1];
                    }
                    else{
                        i++;
                    }
                }
                else {//second player; first possible eat move
                      if (possibleMoves.get(i)[1].getPieces().get(0).getId()==0){
                          res[0]=possibleMoves.get(i)[0];
                          res[1]=possibleMoves.get(i)[1];
                          break;
                      }
                      else{
                          i++;
                      }
                }

            }
        }
        return res;
    }


//_____________________________________________________________________________________________________________________

    public void printSummary(){
        System.out.println("Player #: "+id+
        "\nPrimeingMoves: "+primeMovesCounter
        +"\nBlitzingMoves: "+blitzMoveCounter
        +"\nTurnsPassed: "+turnsPassedCounter
        +"\notherMoves: "+otherMovesCounter);
    }

    public void resetCounters(){
         primeMovesCounter=0;
         blitzMoveCounter=0;
         turnsPassedCounter=0;
         otherMovesCounter=0;
    }

    
}
