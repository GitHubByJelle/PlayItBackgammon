package src.AI;


import World.Board;
import World.Piece;
import World.Player;
import World.Space;

import java.sql.SQLOutput;
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
        int max = 0;
        int maxFrom = 0;
        int maxTo = 0;
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == this.id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    System.out.println("What the fuck is going on: " + validMoves);
                    System.out.println("Size " + validMoves.size());
                    if (validMoves.size() > 0) {
                        if (validMoves.get(0).getId() == validMoves.get(1).getId()) {
                        } else {
                            for (Space v : validMoves) {
                                System.out.println("Why print 2: " + validMoves.size());
                                int score = 0;
                                score = evaluationFunction(space,v);
                                if (score >= max) {
                                    max = score;
                                    maxFrom = space.getId();
                                    maxTo = v.getId();
                                }
                                System.out.println("Move to: " + v.getId() + " for " + score);
                            }
                        }
                    }
                    System.out.println("Valid moves: ");
                    System.out.println("From:  " + space.getId() + " To: " + validMoves);
                    System.out.println("-----------------------------");
                }
            }
        }
        System.out.println("Best Possible Move Is: " + maxFrom + " to " + maxTo + " for " + max);

    }

    public int evaluationFunction(Space from, Space to) {
        int score = 0;
        score += isGoingToKill(from, to);
        score += isGoingHome(from, to);
        score += isOutOfPlay(from, to);
        score += isGoingToBeEaten(from, to);
        return score;
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
        if (to.getId() > 18) {
            System.out.println("Going home");
            return 3;
        } else return 1;
    }

    public int isOutOfPlay(Space from, Space to) {
        if (to.getId() == 26) {
            System.out.println("Going to out");
            return 7;
        } else return 1;
    }

    public int isGoingToBeEaten(Space from, Space to) {
        return 1;
    }
}
