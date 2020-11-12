/* package src.AI;

import src.World.Board;
import src.World.Die;
import src.World.Player;
import src.World.Space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaBot extends Player.Bot {



    private  final double HEURISTIC_COEFFICIENT = .055;
    private  final int DEFAULT_DEPTH = 3;
    private final Player opponent;
    private int initialDepth = 0;
    // private double UPPERBOUND;
    // private double LOWERBOUND;

    public AlphaBetaBot(Board board) {
        super(0);
        setBoard(board);
        System.out.println("Test tostring");
        System.out.println(this.B);
        System.out.println("-------------------------------------------------");
    }

    // MAIN ALPHA BETA PRUNING METHOD, MOVE IS MADE DUTING THE CALL OF THIS METHOD
    // @PARAM CURRENT SPACE S, AND AN DIE OBJECT
    private static void alpha_beta_pruning(Space s, Die die){
      die.generateDie();
      int diceRoll1 = die.getDieList[0];
      int diceRoll2 = die.getDieList[1];
      int to_ID = minMove(s, diceRoll1, diceRoll2, Integer.MAXIMUM, Integer.MINIMUM, initialDepth)[1];
      makeMove(s.getID, to_ID);
    }
    //FUNCTION OF MAX MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MAXIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private static double[] maxMove(Space currentSpace, int diceRoll1, int diceRoll2, double alpha, double beta, int depth){
      if(depth == DEFAULT_DEPTH){
        return new double[]{evaluationFunction(currentSpace, currentSpace)/18, currentSpace.getID()};
      }
      Arraylist<Space> stateArray= getAllSpaces(currentSpace, diceRoll1, diceRoll2);
      double max_util = Integer.MINIMUM;
      Space to = null;
      for(Space s : stateArray){
        if(alpha < expectiMaxMin(s, 0, alpha, beta, depth-1)){
          max_util = expectiMaxMin(s, 0, alpha, beta, depth-1);
          alpha = max_util;
          to = s;
        }
      }
      return new double[]{max_util, to.getID()};
    }
    //FUNCTION OF MIN MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MINIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private static double minMove(Space currentSpace, int diceRoll1, int diceRoll2, double alpha, double beta, int initialDepth){
      if(depth == DEFAULT_DEPTH){
        return new double[]{evaluationFunction(currentSpace, currentSpace)/18, currentSpace.getID()};
      }
      Arraylist<Space> stateArray= getAllSpaces(currentSpace, diceRoll1, diceRoll2);
      double min_util = Integer.MINIMUM;
      for(Space s : stateArray){
        if(beta > expectiMaxMin(s, 0, alpha, beta, depth-1)){
          min_util = expectiMaxMin(s, 0, alpha, beta, depth-1);
          beta = min_util;
        }
      }
      return min_util;
    }
    // FUNCTION OF CALCULATING EXPECIMINMAX VALUE
    // @PARAM: SPACE S, PLAYER INDEX(O REPRESENTS MIN, 1 REPRESENTS MAX), CURRENT VALUE OF ALPHA, CURRENT VALUE OF BETA, CURRENT DEPTH
    //@RETURN  A DOUBLE VALUE REPRESENTING THE EXPECIMINMAX VALUE
    private static double expectiMaxMin(Space s, int player, double alpha, double beta, int depth){
      double expectiValue = 0;
      int count = 0;

      if(player == 0){
        for(int i=1;i<=6; i++){
          for(int j=i+1; j<=6; j++){
            if(expectiValue + (beta + (14-count)*UPPERBOUND)<alpha){
              return expectiValue;
            }
            double value = minMove(s, i, j, alpha, beta, depth)[0];
            expectiValue += value/18;
            count ++;
          }
        }
      }
      else if(player == 1){
        for(int i=1;i<=6; i++){
          for(int j=i+1; j<=6; j++){
            if(expectiValue + (beta + (14-count)*LOWERBOUND)>beta){
              return expectiValue;
            }
            double value = maxMove(s, i, j, alpha, beta, depth)[0];
            expectiValue += value/18;
            count++;
          }
        }
      }
      return expectiValue;
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


 */