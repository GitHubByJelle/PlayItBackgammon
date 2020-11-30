package AI;

import World.Board;
import World.Die;
import World.Player;
import World.Space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaBot extends Player.Bot {
    private static Move[] final_move = new Move[2];
    private double[] moveQuality;

    char[] ACTION_DEPTH = {'a', 'p', 'i', 'p'};
    private int[][] DIES_COMBINATION = new int[21][2];

    List<Move> possibleMoves = new ArrayList<>();
    private final AlphaBetaBot opponent = null;
    private static int initialDepth = 0;
    private static int DEFAULT_DEPTH = 1;

    public AlphaBetaBot(int id) {
        super(id);
        System.out.println("Test tostring");
        System.out.println(this.B);
        System.out.println("-------------------------------------------------");
    }
    
    @Override
    public String getName() {
        return "AlphBetaBot";
    }
    
    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        alpha_beta_pruning_result();
        pauseBot();
        B.getGameLoop().repaintBV();

    }

    // MAIN ALPHA BETA PRUNING METHOD, MOVE IS MADE DUTING THE CALL OF THIS METHOD
    // @PARAM CURRENT SPACE S, AND AN DIE OBJECT
    private void alpha_beta_pruning_result(){
    	if(this.id == 0) {  
    		double expecMinMax = 0;
    		ArrayList<Move> moves = generateMoves(0);
    		System.out.println(moves.size());
    		for(Move move1: moves) {
    			for(Move move2: moves) {
    				double util = 
    						maxMove(move1, move2, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth);
    				if(expecMinMax<util) {
    					expecMinMax = util;
    					final_move[0] = move1;
    					final_move[1] = move2;
    				}
    			}
    		}
    		makeMove(final_move[0]);
    		makeMove(final_move[1]);
    	}
    	else {
    		double expecMinMax = 0;
    		ArrayList<Move> moves = generateMoves(0);
    		for(Move move1: moves) {
    			for(Move move2: moves) {
    				double util = 
    						minMove(move1, move2, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth);
    				if(util<expecMinMax) {
    					expecMinMax = util;
    					final_move[0] = move1;
    					final_move[1] = move2;
    				}
    			}
    		}
    		makeMove(final_move[0]);
    		makeMove(final_move[1]);
    	}
    }
    
    //FUNCTION OF MAX MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MAXIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double maxMove(Move move1, Move move2, double alpha, double beta, int depth){
		if (depth == DEFAULT_DEPTH) {
			return 100;
//        	return evaluationFunction();
		}
		double max_util = 0;
		makeMove(move1);
		makeMove(move2);
		for (int i = 0; i < 15; i++) {
			if (alpha < expectiMaxMin_alpha_beta(alpha, beta, depth - 1, 1)) {
				max_util = expectiMaxMin_alpha_beta(alpha, beta, depth - 1, 1) / 18;
				alpha = max_util;
			}
		}
		undoMove(move1);
		undoMove(move2);
		return max_util;
    }

    //FUNCTION OF MIN MOVE
    // @PARAM: CURRENT SPACE, FIRST DICE ROLL, SECOND DICE ROLL, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MINIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double minMove(Move move1, Move move2, double alpha, double beta, int depth){
		if (depth == DEFAULT_DEPTH) {
			return 100;
//        	return evaluationFunction();
		}
		double min_util = 0;
		makeMove(move1);
		makeMove(move2);
		for (int i = 0; i < 15; i++) {
			if (beta > expectiMaxMin_alpha_beta(alpha, beta, depth - 1, 0)) {
				min_util = expectiMaxMin_alpha_beta(alpha, beta, depth - 1, 0) / 18;
				beta = min_util;
			}
		}
		undoMove(move1);
		undoMove(move2);
		return min_util;
    }

    // FUNCTION OF CALCULATING EXPECIMINMAX VALUE
    // @PARAM: SPACE S, PLAYER INDEX(O REPRESENTS MIN, 1 REPRESENTS MAX), CURRENT VALUE OF ALPHA, CURRENT VALUE OF BETA, CURRENT DEPTH
    //@RETURN  A DOUBLE VALUE REPRESENTING THE EXPECIMINMAX VALUE
    private double expectiMaxMin_alpha_beta(double alpha, double beta, int depth, int player){
    	double expectiValue = 0;
    	// if it is min's turn
    	if(player == 1){
    		ArrayList<Move> moves = generateMoves(1);
    		double min = Integer.MAX_VALUE;
    	  	for(Move move1: moves){
    	  	  	for(Move move2: moves){
    	  	  		min = Math.min(min, minMove(move1,move2, alpha, beta, depth));
    	  	  	}
    	  	}
    	  	expectiValue = min;
    	}
    	// if it is max's turn
    	else if(player == 0){
    		ArrayList<Move> moves = generateMoves(1);
    		double max = Integer.MIN_VALUE;
    		for(Move move1: moves){
    			for(Move move2: moves){
    				max = Math.max(max, minMove(move1,move2, alpha, beta, depth));
    			}
    		}
    		expectiValue = max;
    	}
    	return expectiValue;
    }

    //another version
    public ArrayList<Move> generateMoves(int id) {
    	ArrayList<Move> possible_moves = new ArrayList<>();
        Space[] allSpaces = this.B.getSpaces();
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    if (validMoves.size() > 1) {
                        if (validMoves.get(0).getId() != validMoves.get(1).getId()) {
                        } else {
                            for (Space v : validMoves) {
                                int score = 0;
                                score = evaluationFunction(space, v);
                                Move move = new Move(space.getId(), v.getId(), score);
                                possible_moves.add(move);
                            }
                        }
                    }
                }
            }
        }
		return possible_moves;
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
        this.B.BotMove(move.from, move.to);

    }

    public void makeMove(int from, int to) {
        this.B.BotMove(from, to);

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
