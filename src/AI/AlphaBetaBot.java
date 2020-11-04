package src.AI;


import src.World.Board;
import src.World.Piece;
import src.World.Player;
import src.World.Space;

import java.util.ArrayList;
import java.util.Arrays;

public class AlphaBetaBot extends Player.Bot {

    public AlphaBetaBot(Board board) {
        super(0);
        setBoard(board);

    }

    public void getAllPossibleMoves() {
        Space[] allSpaces = this.B.getSpaces();
        System.out.println(Arrays.toString(this.B.getDie().getCurRoll()));
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == this.id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    int max = 0;
                    for (Space v : validMoves){
                        System.out.println("Why print 2: " + validMoves.size());
                        int score = 0;
                        score += isGoingToKill(space,v);
                        score += isGoingHome(space,v);
                        score += isOutOfPlay(space,v);
                        score += isGoingToBeEaten(space,v);
                        if(score >= max){
                            max = score;
                        }
                        System.out.println("Move to: " + v.getId() + " for " + score);
                    }
                    System.out.println("Valid moves: ");
                    System.out.println("From:  " + space.getId() + " To: " + validMoves);
                }
            }
        }
    }

    public void evaluationFunction(Space from, ArrayList<Space> validMoves) {

    }

    public int isGoingToKill(Space from, Space to) {
        if (to.getSize() == 1) {
            if (to.getPieces().get(0).getId() != this.id) {
                System.out.println("Going to kill");
                return 5;
            }
        } else return 1;
      return 1;
    }

    public int isGoingHome(Space from, Space to) {
        if (to.getId() >18) {
            System.out.println("Going home");
            return 3;
        } else return 1;
    }

    public int isOutOfPlay(Space from, Space to){
        if(to.getId() == 26){
            System.out.println("Going to out");
            return 7;
        }else return 1;
    }
    public int isGoingToBeEaten(Space from, Space to){
        return 1;
    }
}
