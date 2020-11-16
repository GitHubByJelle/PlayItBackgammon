package AI;

import World.Player;
import World.Space;

import java.util.ArrayList;


public class PBE extends Player.Bot{


    private boolean eaten=false;
    final double [] weights=new double[]{2,-.2,1,-1};//{number of this player's walls, number of this player's single pieces, number of opponent eaten pieces, high stacks(>=4) in home space,}

    public PBE(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "PBE";
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        moveChoice();
       pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice() {
        ArrayList<Space> possFrom =getPossibleFrom();
        ArrayList<Space[]> possibleMoves = getPossibleMoves(possFrom);
        Space[] bestMove = new Space[2];//move selected

        if (possFrom.contains(B.getSpaces()[B.getGameLoop().getSlainSpace()])) {//force move piece out of slain space
            for (int i = 0; i < possibleMoves.size(); i++) {
                if (possibleMoves.get(i)[0].getId() == B.getGameLoop().getSlainSpace()) {
                    System.out.println("Piece revived");
                    bestMove[0] = possibleMoves.get(i)[0];
                    bestMove[1] = possibleMoves.get(i)[1];
                    break;
                }
            }
        }else {
            bestMove = getBestMove(possibleMoves);
        }

        if(!moveIsEmpty(bestMove)) {
            B.playerMove(bestMove[0].getId(), bestMove[1].getId());
            System.out.println(bestMove[0].getId()+"," +bestMove[1].getId());
        }else {
            System.out.println("PASS");
            requestPassTurn();
        }


    }

    private Space[] getBestMove(ArrayList<Space[]> possibleMoves) {
        Space[] res= new Space[2];
        double[] scores= new double[possibleMoves.size()];
        int [][] currentBoard = getBoardRep();//{id,num pieces this player, num pieces opp}
     //   System.out.println(printPossMoves(possibleMoves));
       // System.out.println(Arrays.deepToString(currentBoard).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        int [][] home= getHome(currentBoard);
        int numWalls = countWalls(home, 2);
        int singles = countSingles(currentBoard);
        int eatenOpp= countEatenOpp(currentBoard);
        int highStacks= countHighStacks(home);
        double currentScore = evaluate(numWalls,singles,eatenOpp, highStacks);
        int indexBest =-1;



        for( int i =0 ;i<possibleMoves.size();i++){
          //  System.out.println(Arrays.deepToString(possibleMoves.get(i)));
            simulateMove(currentBoard, possibleMoves.get(i));
          //  System.out.println(Arrays.deepToString(currentBoard).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
            home=getHome(currentBoard);
            numWalls = countWalls(home, 2);
            singles = countSingles(currentBoard);
            eatenOpp= countEatenOpp(currentBoard);
            highStacks= countHighStacks(home);
            scores[i]= evaluate(numWalls,singles,eatenOpp, highStacks);
            unDoMoveSim(currentBoard, possibleMoves.get(i));
        //    System.out.println(Arrays.deepToString(currentBoard).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        }

        //System.out.println("Scores "+Arrays.toString(scores));

        indexBest=checkMoves(scores, currentScore);//betterMove
        if(indexBest!=-1){
            res= possibleMoves.get(indexBest);
        }else{
            indexBest=checkMoves(scores, -1);//decentMove
            if(indexBest!=-1){
                res= possibleMoves.get(indexBest);
            }
        }


        return res;
    }

    private int countHighStacks(int[][] home) {
        int res =0;
        int totalPieces=0;
        for(int i=0;i<home.length; i++){
            if(home[i][1]>=4){
                ++res;
            }
            totalPieces+=home[i][1];
        }
        if(totalPieces==15){
            res=0;
        }
        return res;
    }

    private int checkMoves(double[] scores, double t) {
        int indexBest=-1;
        for(int i=0;i<scores.length;i++){
            if(scores[i]>=t){
                indexBest=i;
                t=scores[i];
            }
        }
        return indexBest;

    }

    private int[][] getHome(int[][] currentBoard) {
        int[][] homeSpaces= new int[6][3];//per space, [id,number of this player's pieces] {{6,5},{5,0},{4,0}{3,0},{2,0},{1,2}}
        int spaceIndex=0;
        if(id==0){
            for(int i=19;i<=24;i++){
                homeSpaces[spaceIndex]= new int[]{i, currentBoard[i-1][1]};
                ++spaceIndex;
            }
        }else{
            for(int i=6;i>=1;i--){
                homeSpaces[spaceIndex]= new int[]{i, currentBoard[i-1][1]};
                ++spaceIndex;
            }
        }
        return homeSpaces;
    }

    private String printPossMoves(ArrayList<Space[]> possibleMoves) {
        String res="";
        for( int i=0;i<possibleMoves.size();i++){
            res+=possibleMoves.get(i)[0].getId() +" "+
                    possibleMoves.get(i)[1].getId()+"\n";
        }
        return res;
    }

    private int countEatenOpp(int[][] currentBoard) {
        int res=0;
        for( int i=0;i<currentBoard.length;i++){
            res+=currentBoard[i][2];
        }
        return 15-res;
    }

    private int countSingles(int[][] currentBoard) {
        int res=0;
        for( int i=0;i<currentBoard.length;i++){
            if(currentBoard[i][1]==1){
                ++res;
            }
        }
        return res;
    }

    private int [][] getBoardRep(){
        int[][] board = new int[24][3];//{id,num pieces this player, num pieces opp}
        for(int i=0;i<board.length;i++){
            board[i][0]=B.getSpaces()[i+1].getId();
            if(!B.getSpaces()[i+1].isEmpty() && B.getSpaces()[i+1].getPieces().get(0).getId()==id)
                board[i][1]=B.getSpaces()[i+1].getSize();
            else
                board[i][1]=0;

            if(!B.getSpaces()[i+1].isEmpty() && B.getSpaces()[i+1].getPieces().get(0).getId()!=id)
                board[i][2]=B.getSpaces()[i+1].getSize();
            else
                board[i][2]=0;


        }
        return board;
    }



    private boolean moveIsEmpty(Space[] m) {
        return m[0]==null || m[1]==null;
    }




    private void simulateMove(int[][] home, Space[] move) {
        for(int i=0;i<home.length;i++){
            if(home[i][0]==move[0].getId()){
                --home[i][1];
            }else if(home[i][0]==move[1].getId()){
                ++home[i][1];
                if(home[i][2]==1){
                    --home[i][2];
                    eaten=true;
                }
            }
        }
    }

    private void unDoMoveSim(int[][] home, Space[] move) {
        for(int i=0;i<home.length;i++){
            if(home[i][0]==move[0].getId()){
                ++home[i][1];
            }else if(home[i][0]==move[1].getId()){
                --home[i][1];
                if(eaten){
                    eaten=false;
                    ++home[i][2];
                }
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


    private double evaluate(int numWalls, int singles,int eatenOpp, int highStacks){
        return weights[0]*numWalls+weights[1]*singles+ weights[2]*eatenOpp + weights[3]*highStacks;
    }

}
