package src.AI;

import src.World.Board;
import src.World.Die;
import src.World.Player;
import src.World.Space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaBot extends Player.Bot {




    public AlphaBetaBot(Board board) {
        super(0);
        setBoard(board);
        System.out.println("Test tostring");
        System.out.println(this.B);
        System.out.println("-------------------------------------------------");
    }

    public void getBestPossibleMoves() {
        Space[] allSpaces = this.B.getSpaces();
        System.out.println(Arrays.toString(this.B.getDie().getCurRoll()));
        int max = 0;
        int maxFrom = 0;
        int maxTo = 0;
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == this.id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    if (validMoves.size() > 0) {
                        if (validMoves.get(0).getId() == validMoves.get(1).getId()) {
                        } else {
                            for (Space v : validMoves) {
                                int score = 0;
                                score = evaluationFunction(space, v);
                                if (score >= max) {
                                    max = score;
                                    maxFrom = space.getId();
                                    maxTo = v.getId();
                                }
                            }
                        }
                    }
                }
            }
        }
        makeMove(maxFrom, maxTo);
    }

    public void makeMove(int from, int to) {
        this.B.BotMove(from, to);
        System.out.println(this.B);
        System.out.println("Make move successfully");
    }

    public void undoMove(boolean didKill, boolean didOut, int from, int to, int id) {
        makeMove(to, from);
        if (didKill) {
            if (id == 0) {
                makeMove(0,to);
            } else {
                makeMove(25,to);
            }
        }
        if (didOut) {
            if (id == 0) {

            } else {

            }
        }
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
        if (to.getSize() <= 1) {
            if (someWhatSafe()) {
                return -1;
            } else {
                return -4;
            }
        }
        return 1;
    }

    private boolean someWhatSafe() {
        // Check around for enemies, if no enemies nearby, its somewhat safe
        return false;
    }
    private static double min(double... values) {
        double min = Double.MAX_VALUE;
        for (double value : values) {
            if (value < min)
                min = value;
        }
        return min;
    }

    private static double max(double... values) {
        double max = -Double.MAX_VALUE;
        for (double value : values) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private static double weightedAverage(List<Double> values,Die die) {
        double weightedSum = 0.;
        double coefficientSum = 0.;
        int i = 0;
        for (Double value : values) {
            double coefficient = die.probability();
            weightedSum += value * coefficient;
            coefficientSum += coefficient;
        }
        return weightedSum / coefficientSum;
    }

}
