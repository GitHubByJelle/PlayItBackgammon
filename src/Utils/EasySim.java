package Utils;

import AI.RandomBot;
import AI.SimpleBot;
import World.Board;
import World.Player;
import World.Space;

import java.util.ArrayList;
import java.util.Arrays;

public class EasySim {
    static int [][] boardRep;//{id, this player pieces, opponent pieces}
    static boolean eaten=false;
    public static void main(String [] args){
        Board b= new Board();
        Player a= new RandomBot(0);
        Player c= new SimpleBot(1);
        c.setBoard(b);
        a.setBoard(b);
        b.setPlayers(a,c);
        int[][] rep = setBoardRep(b, 1);
        printRep();

        simulateMove(rep,1,2);
        printRep();

        unDoMoveSim(rep, 1,2);
        printRep();


        System.out.println(Arrays.toString(getPossMoves(new int[]{1,2},1,0,rep).toArray()));


    }

    public static void simulateMove(int[][] board, int from, int to) {
        for(int i=0;i<board.length;i++){
            if(board[i][0]==from){
                --board[i][1];
            }else if(board[i][0]==to){
                ++board[i][1];
                if(board[i][2]==1){
                    --board[i][2];
                    eaten=true;
                }
            }
        }
    }
    public static void unDoMoveSim(int[][] board, int from, int to) {
        for(int i=0;i<board.length;i++){
            if(board[i][0]==from){
                ++board[i][1];
            }else if(board[i][0]==to){
                --board[i][1];
                if(eaten){
                    eaten=false;
                    ++board[i][2];
                }
            }
        }
    }


    public static int[][] setBoardRep(Board b, int id){
        boardRep = new int[27][3];//{id,num pieces this player, num pieces opponent}
        for(int i=0;i<boardRep.length-1;i++){
            boardRep[i][0]=b.getSpaces()[i].getId();
            if(!b.getSpaces()[i].isEmpty() && b.getSpaces()[i].getPieces().get(0).getId()==id)
                boardRep[i][1]=b.getSpaces()[i].getSize();
            else
                boardRep[i][1]=0;

            if(!b.getSpaces()[i].isEmpty() && b.getSpaces()[i].getPieces().get(0).getId()!=id)
                boardRep[i][2]=b.getSpaces()[i].getSize();
            else
                boardRep[i][2]=0;

        }

        boardRep[26][0]= 26;
        boardRep[26][1]=b.getPlayer1().getPiecesOutOfPlay();
        boardRep[26][2]= b.getPlayer2().getPiecesOutOfPlay();
        return boardRep;
    }



    public static void printRep(){
        for( int i=0;i< boardRep.length;i++){
            System.out.println(i+" "+ Arrays.toString(boardRep[i]));
        }
    }



    public static ArrayList<Integer > getPossMoves(int [] dieroll, int selected, int playerID, int [][]boardRep){
        ArrayList<Integer> res = new ArrayList<>();

        int target;
        int bigger=0;
        int smaller=0;

        for (int i = 0; i < dieroll.length; i++) {
            if(selected + dieroll[i] < 25 && selected + dieroll[i] > 0){//check for bounds
                target = selected + dieroll[i];
                if (validityCheck(selected, target, playerID)) {
                    res.add(selected + dieroll[i]);
                }

            } else {
                //check if all the pieces are home in case the rolls can take the current piece out of play(eaten Space)
                if (!isEmpty(selected) &&allPiecesHome(playerID)) {

                    if (selected>6) {
                        for (int j = 24; j > 18; j--) {
                            if ( boardRep[j][1] +boardRep[j][2] > 0) {
                                bigger = j;
                            }
                        }

                        if (dieroll.length > 1) {
                            if ((25 - selected) == dieroll[0] || (25 - selected) == dieroll[1])
                                res.add(26);

                            else if (selected== bigger && dieroll[0] > (25 - selected) || selected== bigger && dieroll[1] > (25 - selected)) {
                                res.add(26);
                            }
                            else if (selected > bigger && dieroll[0] > (25 - selected) && selected > bigger && dieroll[1] > (25 - selected)) {
                                res.remove(26);
                            }
                        } else {
                            if ((25 - selected) == dieroll[0])
                                res.add(26);

                            else if (selected == bigger && dieroll[0] > (25 - selected)) {
                                res.add(26);
                            } else if (selected > bigger && dieroll[0] > (25 - selected)) {
                                res.remove(26);
                            }
                        }
                    } else {
                        for (int j = 1; j < 6; j++) {
                            if (boardRep[j][1] +boardRep[j][2] > 0) {
                                bigger = j;
                            }
                        }
                        if (dieroll.length > 1) {
                            if (selected == Math.abs(dieroll[0]) || selected == Math.abs(dieroll[1]))
                                res.add(26);

                            else if (selected == bigger && Math.abs(dieroll[0]) > selected|| selected == bigger && Math.abs(dieroll[1]) > selected ) {
                                res.add(26);
                            }
                            else if (selected < bigger && Math.abs(dieroll[0]) > selected&& Math.abs(dieroll[1]) > selected) {
                                res.remove(26);
                            }
                        } else {
                            if (selected == Math.abs(dieroll[0]))
                                res.add(26);

                            else if (selected == bigger && Math.abs(dieroll[0]) > selected) {
                                res.add(26);
                            }
                            else if (selected< bigger && Math.abs(dieroll[0]) > selected) {
                                res.remove(26);
                            }

                        }
                    }
                }
            }
        }


        return res;
    }



    public static boolean validityCheck(int selected, int target, int playerID) {
        return !isEmpty(selected) && (isEmpty(target) || //if the target space is empty
                piecesOfSameColor(selected, target) || //if the space has pieces of the same color
                pieceCanBeEaten(selected, target, playerID)); //target has one piece and its color doesnt match
    }

    private static boolean pieceCanBeEaten(int selected, int target, int playerID) {
        return (boardRep[target][++playerID]==1 && !piecesOfSameColor(selected,target));
    }

    private static boolean isEmpty(int s){
        return boardRep[s][1]+ boardRep[s][2]==0;
    }

    private static boolean piecesOfSameColor(int selected, int target){
        if((boardRep[selected][1]!=0 && boardRep[target][1]!=0)||(boardRep[selected][2]!=0 && boardRep[target][2]!=0)){
            return true;
        }else {
            return false;
        }
    }


    private static boolean allPiecesHome(int id){
        int numPieces=0;
        if(id==0){
            // non-home space + out of play space [1-18] +0
            for(int i=0; i<19;i++){
                numPieces+=boardRep[i][1];
            }
        }else{
            //id==1
            // non-home space + out of play space [7-24] +25
            for(int i=7; i<26;i++){
                numPieces+=boardRep[i][2];
            }
        }

        if(numPieces!=0)return true;
        else return false;
    }


}
