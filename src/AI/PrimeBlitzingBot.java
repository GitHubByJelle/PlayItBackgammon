package AI;

import World.Player;
import World.Space;
import java.util.ArrayList;

public class PrimeBlitzingBot extends Player.Bot{

    public PrimeBlitzingBot(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "PrimeBlitzingBot";
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
       //choice of blitzing or priming
        moveChoice();
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice(){
        //get the possible moves we can make
        ArrayList<Space[]> possibleMoves = getPossibleMoves(getPossibleFrom());
        Space[] bestMove = possibleMoves.get(0);//move selected
        //make a priming move(Alaa's)
        Space[] bestPrimingMove = makePrimingMove(possibleMoves);
        //make Blitzing move(Adaia's)
            //notes for adaia,
                //try to return a Space[] of size 2 [from,to]





        //here would be where we decide whether to use B move or P move
        if(!moveIsEmpty(bestPrimingMove)) {
            System.out.println("primingMove selected");
            bestMove = bestPrimingMove;
        }else{
            System.out.println("Question Life Decisions & the purpose of humans; pt2/?");//imbeingreallybleakheresorrynotsorryxd
        }
        B.playerMove(bestMove[0].getId(),bestMove[1].getId());
    }

    //returns whether any part of the move ==null(ie it was never set to a value
    private boolean moveIsEmpty(Space[] m) {
        return m[0]==null || m[1]==null;
    }

    private Space[] makePrimingMove(ArrayList<Space[]> possibleMoves){
        int wallSize=2;
        int numWalls=3;
        Space[] res= new Space[2];//array of a [from,to] space
        int [][] currentHomeSpace= currentHome();
        int currentNumWalls = countWalls(currentHomeSpace, wallSize);
        if(currentNumWalls>=numWalls){
            //A
        }else{
            //B
            res= evaluatePossPrimingMove(currentHomeSpace,currentNumWalls,possibleMoves);
        }

            //check the home space %
                //count number of walls already there
                    //A:if we have desired number then break and let it decide other move
                    //B:else we should evaluate the moves and the walls each move could make and decide which move to carry out based on that




        return res;
    }

    private Space[] evaluatePossPrimingMove(int[][] currentHomeSpace, int currentNumWalls, ArrayList<Space[]> possibleMoves) {
        Space[] res=new Space[2];

        return res;
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
        int[][] homeSpaces= new int[6][2];//per space, [id,number of this player's pieces]
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
            for(int i=1;i<=6;i++){
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
    private void blitzing(){

    }

    
}
