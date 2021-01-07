package AI.AlphaBeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import World.Piece;
import World.Player;
import World.Space;

public class ABbot extends Player.Bot {
    private ArrayList<Move> final_moves = new ArrayList<>();
    public double[] evaluator = {0.27492492486083797, 0.7603447308733254, 0.6324866448907414, 0.18347935996872392, 0.3290528252791358};
    List<Move> possibleMoves = new ArrayList<>();
    private ABbot opponent = null;
    private static int initialDepth = 0;
    private static int MAX_DEPTH = 3;
    public static int count = 0;

    public ABbot(int id) {
        super(id);
//        System.out.println("Test tostring");
//        System.out.println(this.B);
//        System.out.println("-------------------------------------------------");
    }

    public void setOpponent(ABbot opponent) {
        this.opponent = opponent;
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

//    private void alpha_beta_pruning_result() {
//		double expectAB;
//		if(this.id == 0)
//			expectAB = Integer.MIN_VALUE;
//		else
//			expectAB =  Integer.MAX_VALUE;
//		ArrayList<Move> possibleMoves = generateMoves2();
//		if(possibleMoves.size()==0) {
//			this.requestPassTurn();
//		}
//		for(int move1=0; move1<possibleMoves.size(); move1++) {
//			this.B.BotMove(possibleMoves.get(move1).from, possibleMoves.get(move1).to);
//			for(int move2=move1+1; move2<possibleMoves.size() && move1!=move2; move2++) {
//				this.B.BotMove(possibleMoves.get(move2).from, possibleMoves.get(move2).to);
//				double value = expect_alpha_beta(initialDepth+1, Integer.MIN_VALUE, Integer.MAX_VALUE, this.opponent);
//				if(this.id == 0) {
//					if(value > expectAB) {
//						expectAB = value;
//						final_moves = new ArrayList<>();
//						final_moves.add(0,possibleMoves.get(move1));
//						final_moves.add(1,possibleMoves.get(move1));
//					}
//				}
//				else {
//					if(value < expectAB) {
//						expectAB = value;
//						final_moves = new ArrayList<>();
//						final_moves.add(0,possibleMoves.get(move1));
//						final_moves.add(1,possibleMoves.get(move1));
//					}
//				}
//				this.undoMove(possibleMoves.get(move2));
//			}
//			this.undoMove(possibleMoves.get(move1));
//		}
//	}
//	
//	private double expect_alpha_beta(int depth, double alpha, double beta, ABbot player) {
//		if (depth == MAX_DEPTH) {
//			return EvaluationFunction();
//		}
//		double expectMinMax = 0.0;
//		double util = 0.0;
//		if(player.id == 0)
//			util = Integer.MIN_VALUE;
//		else
//			util = Integer.MAX_VALUE;
//			
//		ArrayList<Move> possibleMoves = player.generateMoves2();
//		if(possibleMoves.size()==0) {
//			player.requestPassTurn();
//		}
//		for(int move1=0; move1<possibleMoves.size(); move1++) {
//			player.B.BotMove(possibleMoves.get(move1).from, possibleMoves.get(move1).to);
//			for(int move2=0; move2<possibleMoves.size() && move1!=move2; move2++) {
//				player.B.BotMove(possibleMoves.get(move2).from, possibleMoves.get(move2).to);
//				if (player.id == 0) {
//					double value = expect_alpha_beta(depth + 1, alpha, beta, player.opponent);
//					if(value > util) {
//						util = value;
//						alpha = util;
//					}
//				} 
//				else {
//					double value = expect_alpha_beta(depth + 1, alpha, beta, player.opponent);
//					if(value < util) {
//						util = value;
//						beta = util;
//					}
//				}
//				if(beta-alpha <= 0.00001) 
//					return beta;
//				player.undoMove(possibleMoves.get(move2));
//			}
//			player.undoMove(possibleMoves.get(move1));
//			expectMinMax += 1/18 * util;
//		}
//		return expectMinMax;
//	}

    // MAIN ALPHA BETA PRUNING METHOD, MOVE IS MADE DUTING THE CALL OF THIS METHOD
    // @PARAM CURRENT SPACE S, AND AN DIE OBJECT
    public void alpha_beta_pruning_result() {
    	try {
	        if (this.id == 0) {
	            final_moves = new ArrayList<>();
	            double expecMinMax = Integer.MIN_VALUE;
	            ArrayList<Move> moves = generateMoves2();
	            if (moves.size() == 0)
	                this.requestPassTurn();
	            for (int i = 0; i < 2; i++) {
	                Move final_move = null;
	                expecMinMax = Integer.MIN_VALUE;
	                Collections.shuffle(moves);
	                for (Move move : moves) {
	                    double util = maxMove(move, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth, this);
	                    if (expecMinMax < util) {
	                        expecMinMax = util;
	                        final_move = move;
	                    }
	                }
	                final_moves.add(final_move);
	            }
	        } else if (this.id == 1) {
	            final_moves = new ArrayList<>();
	            double expecMinMax = Integer.MAX_VALUE;
	            Move final_move = null;
	            ArrayList<Move> moves = generateMoves2();
	            Collections.shuffle(moves);
	            if (moves.size() == 0)
	                this.requestPassTurn();
	            for (int i = 0; i < 2; i++) {
	                for (Move move : moves) {
	                    double util = minMove(move, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth, this);
	                    if (util < expecMinMax) {
	                        expecMinMax = util;
	                        final_move = move;
	                    }
	                }
	                final_moves.add(final_move);
	            }
	        }
    	}
    	catch(Exception e)
    	{}
    }

    //FUNCTION OF MAX MOVE
    private double maxMove(Move move, double alpha, double beta, int depth, ABbot player) {
        if (move == null)
            player.requestPassTurn();
        if (depth == MAX_DEPTH) {
            return EvaluationFunction();
        }
        double max_util = Integer.MIN_VALUE;
        player.B.BotMove(move.from, move.to);
        for (int i = 0; i < 14; i++) {
            double util = expectiMaxMin_alpha_beta(alpha, beta, depth + 1, player.opponent);
            if (alpha < util / 18) {
                alpha = max_util;
                max_util += util / 18;
            }
        }
        player.undoMove(move);
        return max_util;
    }

    //FUNCTION OF MIN MOVE
    private double minMove(Move move, double alpha, double beta, int depth, ABbot player) {
        if (move == null) {
            player.requestPassTurn();
        }
        if (depth == MAX_DEPTH) {
            return EvaluationFunction();
        }
        double min_util = Integer.MAX_VALUE;
        player.B.BotMove(move.from, move.to);
        for (int i = 0; i < 14; i++) {
            double util = expectiMaxMin_alpha_beta(alpha, beta, depth + 1, player.opponent);
            if (beta > util / 18) {
                beta = min_util;
                min_util += util / 18;
            }
        }
        player.undoMove(move);
        return min_util;
    }

    // FUNCTION OF CALCULATING EXPECIMINMAX VALUE
    private double expectiMaxMin_alpha_beta(double alpha, double beta, int depth, ABbot player) {
        if (beta - alpha <= 0.00001)
            return beta;
        double expectiValue = 0;
        ArrayList<Move> moves = player.generateMoves2();
        Collections.shuffle(moves);
        if (moves.size() != 0) {
            // if it is min's turn
            if (player.id == 1) {
                double min = Integer.MAX_VALUE;


                    for (Move move : moves) {
                        min = Math.min(min, minMove(move, alpha, beta, depth, player));

                }
                expectiValue = min;
            }
            // if it is max's turn
            else if (player.id == 0) {
                double max = Integer.MIN_VALUE;
                for (Move move : moves) {
                    max = Math.max(max, maxMove(move, alpha, beta, depth, player));
                }
                expectiValue = max;
            }
        }else{
            player.requestPassTurn();
        }

        return expectiValue;
    }

    public ArrayList<Move> generateMoves2() {
        ArrayList<Move> possible_moves = new ArrayList<>();
        Space[] allSpaces = this.B.getSpaces();
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == this.id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    if (validMoves.size() > 0) {
                        for (Space v : validMoves) {
                            Move move = new Move(space.getId(), v.getId(), this.id);
                            possible_moves.add(move);
                        }
                    }
                }
            }
        }
//        System.out.println(possible_moves);
        return possible_moves;
    }


    public void makeMove(Move move) {
        this.B.BotMove(move);
    }

    public void undoMove(Move move) {
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
        int temp = move.to;
        move.to = move.from;
        move.from = temp;
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
        this.B.undoBotMove(move);

    }


    // evaluation function to evaluate the whole board status
    public double EvaluationFunction() {
        return (this.OtherPiecesSlain() * evaluator[0] + this.pipCount() * evaluator[1]
                + this.DoneScore() * evaluator[2] + this.DoneBoardScore() * evaluator[3]
                + this.piecesAlone() * evaluator[4]);
    }

    public double OtherPiecesSlain() {
        for (Space space : this.B.getSpaces()) {
            if (space.getSize() == 2 && (space.getPieces().get(0).getId() + space.getPieces().get(1).getId()) == 1) {
                return 1;
            }
        }
        return 0;
    }

    public double pipCount() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double pip = 0;
            for (int i = 1; i <= 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += Math.pow(i, 2);
                    }
                }
            }
            if (this.B.getSpaces()[0].getSize() > 0 && this.B.getSpaces()[0].getPieces().get(0).getId() == 1) {
                pip += this.B.getSpaces()[0].getSize() * 24;
            }
            return -pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        pip += Math.pow(25 - i, 2);
                    }
                }
            }
            return -pip;
        }
    }

    public double DoneScore() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        numberPieces++;
                    }
                }
            }
            return 15 - numberPieces;
        } else {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        numberPieces++;
                    }
                }
            }
            return 15 - numberPieces;
        }
    }

    public double DoneBoardScore() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double score = 0;
            for (int i = 1; i < 7; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    score = +this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        } else {
            double score = 0;
            for (int i = 19; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    score = +this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        }
    }

    public double piecesAlone() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() == 1 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    alone++;
                }
            }
            return -alone;
        } else {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() == 1 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    alone++;
                }
            }
            return -alone;
        }
    }
}
