package AI;

import World.Board;
import World.Die;
import World.Player;
import World.Space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaBot extends Player.Bot {
    private static Move final_move = null;
    private double[] moveQuality;

    char[] ACTION_DEPTH = {'a', 'p', 'i', 'p'};
    private int[][] DIES_COMBINATION = new int[21][2];

    List<Move> possibleMoves = new ArrayList<>();
    private final AlphaBetaBot opponent = (AlphaBetaBot) new Player.Bot(1); //human player opponent
    private static int initialDepth = 0;
    private static int DEFAULT_DEPTH = 3;

    public AlphaBetaBot(Board board) {
        super(0);
        setBoard(board);
        System.out.println("Test tostring");
        System.out.println(this.B);
        System.out.println("-------------------------------------------------");
    }

    // MAIN ALPHA BETA PRUNING METHOD, MOVE IS MADE DUTING THE CALL OF THIS METHOD
    // @PARAM CURRENT SPACE S, AND AN DIE OBJECT
    private void alpha_beta_pruning_result(int diceRoll1, int diceRoll2){
      minMove(diceRoll1, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth);
      makeMove(final_move);
      minMove(diceRoll2, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth);
      makeMove(final_move);
    }
    
    //FUNCTION OF MAX MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MAXIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double maxMove(int diceRoll, double alpha, double beta, int depth){
      // if reaching the maximum depth, return the score of the current board state
      if(depth == DEFAULT_DEPTH){
        return evaluationFunction();
      }
      opponent.generatePossibleMoves(); //generate all the moves for human player
      List<Move> moves = opponent.getAllPossibleMoves(); //get the list of all possible moves
      double max_util = Integer.MIN_VALUE; //a variable to keep track of the min util value
      Move chosen_moves = null; //array to store valid move
      // for all pairs of moves, calculate the expectiMaxMin value and apply alpha_beta pruning
      if(moves.size()==0)
        requestPassTurn();
      else{
        for(int i=0; i<possibleMoves.size();i++){
          if(possibleMoves.get(i).from-possibleMoves.get(i).to==-1*diceRoll){
               chosen_moves = possibleMoves.get(i);
          }
          opponent.makeMove(chosen_moves);
          //apply alpha beta pruning and update the max_util value
          if(alpha < expectiMaxMin_alpha_beta(alpha, beta, depth-1, 1)){
            max_util = expectiMaxMin_alpha_beta(alpha, beta, depth-1, 1);
            alpha = max_util;
            final_move = chosen_moves;
            opponent.undoMove(chosen_moves);
          }
          else{
        	  opponent.undoMove(chosen_moves);
          }
        }
      }
      return max_util;
    }

    //FUNCTION OF MIN MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MINIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double minMove(int diceRoll, double alpha, double beta, int depth){
      // if reaching the maximum depth, return the score of the current board state
      if(depth == DEFAULT_DEPTH){
        return evaluationFunction();
      }
      generatePossibleMoves(); //generate all the moves for the bot
      List<Move> moves = getAllPossibleMoves(); //get the list of all possible moves
      double min_util = Integer.MIN_VALUE; //a variable to keep track of the min util value
      Move chosen_moves = null; //array to store 2 valid moves
      // for all pairs of moves, calculate the expectiMaxMin value and apply alpha_beta pruning
      if(moves.size()==0)
        requestPassTurn();
      else{
        for(int i=0; i<possibleMoves.size();i++){
          if(possibleMoves.get(i).from-possibleMoves.get(i).to==diceRoll){
               chosen_moves = possibleMoves.get(i);
          }
          makeMove(chosen_moves);
          //apply alpha beta pruning and update the max_util value
          if(beta > expectiMaxMin_alpha_beta(alpha, beta, depth-1, 0)){
            min_util = expectiMaxMin_alpha_beta(alpha, beta, depth-1, 0);
            beta = min_util;
            final_move = chosen_moves;
            undoMove(chosen_moves);
          }
          else{
            undoMove(chosen_moves);
          }
        }
      }
      return min_util;
    }

    // FUNCTION OF CALCULATING EXPECIMINMAX VALUE
    // @PARAM: SPACE S, PLAYER INDEX(O REPRESENTS MIN, 1 REPRESENTS MAX), CURRENT VALUE OF ALPHA, CURRENT VALUE OF BETA, CURRENT DEPTH
    //@RETURN  A DOUBLE VALUE REPRESENTING THE EXPECIMINMAX VALUE
    private double expectiMaxMin_alpha_beta(double alpha, double beta, int depth, int player){
      double expectiValue = 0;
      // if it is robot's turn
      if(player == 0){
        for(int i=1;i<=6; i++){
          for(int j=i+1; j<=6; j++){
            double value = minMove(i, alpha, beta, depth)+minMove(j, alpha, beta, depth);
            expectiValue += value/18;
          }
        }
      }
      // if it is human's turn
      else if(player == 1){
        for(int i=1;i<=6; i++){
          for(int j=i+1; j<=6; j++){
            double value = maxMove(i, alpha, beta, depth)+maxMove(j, alpha, beta, depth);
            expectiValue += value/18;
          }
        }
      }
      return expectiValue;
    }



    public void generateMoves() {
        Space[] allSpaces = this.B.getSpaces();
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
                                Move move = new Move(space.getId(), v.getId(), score);
                                possibleMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
    }

    private double expectiminimax(int depth, int currentNodeIndex) {
        double result = 0.;
        List<Move> moves;
        if (initialDepth == -1) initialDepth = depth;
        switch (ACTION_DEPTH[currentNodeIndex]) {
            case 'a':
                generatePossibleMoves();
                moves = this.getAllPossibleMoves();

                if (!moves.isEmpty()) {
                    double[] moveQuality = new double[moves.size()];

                    for (int i = 0; i < moves.size(); i++) {

                        makeMove(moves.get(i));
                        moveQuality[i] = expectiminimax(depth - 1, (currentNodeIndex + 1) % 4);
                        undoMove(moves.get(i));

                    }

                    if (depth == initialDepth) {
                        this.moveQuality = new double[moveQuality.length];
                        System.arraycopy(moveQuality, 0, this.moveQuality, 0, moveQuality.length);
                    }
                    result = max(moveQuality);
                } else {
                        if (depth == initialDepth) moveQuality = new double[0];
                        result = 100;
                }
                break;
            case 'i':
                opponent.generatePossibleMoves();
                moves = opponent.getAllPossibleMoves();
                if (!moves.isEmpty()) {
                    double[] moveQuality = new double[moves.size()];

                    for (int i = 0; i < moves.size(); i++) {
                        opponent.makeMove(moves.get(i));
                        moveQuality[i] = expectiminimax(depth - 1, (currentNodeIndex + 1) % 4);
                        opponent.undoMove(moves.get(i));
                    }

                    result = min(moveQuality);
                } else {
                        if (depth == initialDepth) moveQuality = new double[0];
                        result = -100;
                }
                break;
            case 'p':
                // TODO What to do here?
                if (depth == 0) { // at the root, get the heuristic value
                    result = heuristicValue(currentNodeIndex - 1);
                } else {
                    int[][] dice = DIES_COMBINATION; // For all dies combination,calculate the heuristic value with weight for that die combination to happen, get the state that is most likely to happen,
                    List<Double> values = new ArrayList<>();
                    for (int i = 0; i < dice.length; i += 2) {

                    }
                    result = weightedAverage(values);
                }
        }


        return result;
    }

    public void makeMove(Move move) {
        this.B.botMove(move.from, move.to);

    }

    public void makeMove(int from, int to) {
        this.B.botMove(from, to);

    }

    public void undoMove(Move move) {
        boolean didKill = move.isKill;
        boolean didOut = move.isMoveOut;
        makeMove(move);
        if (didKill) {
            if (id == 0) {
                makeMove(0, move.to);
            } else {
                makeMove(25, move.to);
            }
        }
        if (didOut) {
            makeMove(26, move.to);

        }
    }

    //evaluation function to evaluate the current board state
    public double evaluationFunction(){
//      int ownPiecesOnBoard = this.B.getAllPiecesOnBoard(0);
//      int ownPiecesAtHome = this.B.getAllPiecesAtHome(0);
//
//      int opponentPiecesOnBoard = this.B.getAllPiecesOnBoard(1);
//      int opponentPiecesAtHome = this.B.getAllPiecesAtHome(1);
//
//      return (ownPiecesOnBoard + ownPiecesAtHome - opponentPiecesOnBoard - opponentPiecesAtHome);
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

    private static double min(List<Double> values) {
        double min = Double.MAX_VALUE;
        for (double value : values) {
            if (value < min)
                min = value;
        }
        return min;
    }

    private static double max(List<Double> values) {
        double max = -Double.MAX_VALUE;
        for (double value : values) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private static double weightedAverage(List<Double> values) {
        double weightedSum = 0.;
        double coefficientSum = 0.;
        int i = 0;
        for (Double value : values) {
            double coefficient = this.B.probability();
            weightedSum += value * coefficient;
            coefficientSum += coefficient;
        }
        return weightedSum / coefficientSum;
    }

    public void generatePossibleMoves() {
        possibleMoves = new ArrayList<>();
        generateMoves();
    }

    private double heuristicValue(int currentNodeIndex) {


        int ownPiecesOnBoard = this.B.getAllPiecesOnBoard(0);
        int ownPiecesAtHome = this.B.getAllPiecesAtHome(0);

        int opponentPiecesOnBoard = this.B.getAllPiecesOnBoard(1);
        int opponentPiecesAtHome = this.B.getAllPiecesAtHome(1);

        if (ACTION_DEPTH[currentNodeIndex] == 'a') {
            return (ownPiecesOnBoard + ownPiecesAtHome - opponentPiecesOnBoard - opponentPiecesAtHome);
        } else { // Node.MIN
            return -(ownPiecesOnBoard + ownPiecesAtHome - opponentPiecesOnBoard - opponentPiecesAtHome);
        }
    }

    public List<Move> getAllPossibleMoves() {
        return this.possibleMoves;
    }

    public void generateAllDieCombination() {
        int index = 0;
        for (int i = 1; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                int[] die = {i, j};
                DIES_COMBINATION[index++] = die;
            }
        }
    }
}
