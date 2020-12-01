package AI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import World.Board;
import World.Piece;
import World.Player;
import World.Space;

public class ABbot extends Player.Bot{
	private static Move final_move = null;
	public double[] evaluator = {0.54984985, 1.52068946, 1.26497329, 0.36695872, 0.65810565};
	List<Move> possibleMoves = new ArrayList<>();
    private ABbot opponent = null;
    private static int initialDepth = 0;
    private static int DEFAULT_DEPTH = 3;
    
    public ABbot(Board board, int id) {
        super(id);
        setBoard(board);
        System.out.println("Test tostring");
        System.out.println(this.B);
        System.out.println("-------------------------------------------------");
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

    // MAIN ALPHA BETA PRUNING METHOD, MOVE IS MADE DUTING THE CALL OF THIS METHOD
    // @PARAM CURRENT SPACE S, AND AN DIE OBJECT
    public void alpha_beta_pruning_result(){
    	if(this.id == 0) {  
    		for(int i=0; i<2; i++) {
	    		double expecMinMax = Integer.MIN_VALUE;
	    		ArrayList<Move> moves = generateMoves2();
	    		for(Move move: moves) {
					double util = maxMove(move, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth, this);
					if (expecMinMax < util) {
						expecMinMax = util;
						final_move = move;
					}
	    		}
	    		makeMove(final_move);
    		}
    	}
    	else if(this.id==1){
    		for(int i=0; i<2;i++) {
	    		double expecMinMax = Integer.MAX_VALUE;
	    		ArrayList<Move> moves = generateMoves2();
	    		for(Move move: moves) {
					double util = minMove(move, Integer.MIN_VALUE, Integer.MAX_VALUE, initialDepth, this);
					if (util < expecMinMax) {
						expecMinMax = util;
						final_move = move;
					}
	    		}
	    		makeMove(final_move);
    		}
    	}
    }
    
    //FUNCTION OF MAX MOVE
    // @PARAM: ONE MOVE, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MAXIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double maxMove(Move move, double alpha, double beta, int depth, ABbot player){
		if (depth == DEFAULT_DEPTH) {
			return EvaluationFunction();
		}
		double max_util = Integer.MIN_VALUE;
		player.makeMove(move);
		for (int i = 0; i < 15; i++) {
			double util = expectiMaxMin_alpha_beta(alpha, beta, depth + 1, player.opponent);
			if (alpha < util) {
				max_util = util / 18;
				alpha = max_util;
			}
		}
		player.undoMove(move);
		return max_util;
    }

    //FUNCTION OF MIN MOVE
    // @PARAM: ONE MOVES, ALPHA, BETA, AND CURRENT DEPTH
    // @RETURN A DOUBLE ARRAY CONTAINING MINIMUM UTIL VALUE AND THE ID OF THE SPACE TO WHICH IT SHOULD MOVE TO
    private double minMove(Move move, double alpha, double beta, int depth, ABbot player){
		if (depth == DEFAULT_DEPTH) {
			return EvaluationFunction();
		}
		double min_util = Integer.MAX_VALUE;
		player.makeMove(move);
		
		for (int i = 0; i < 15; i++) {
			double util = expectiMaxMin_alpha_beta(alpha, beta, depth + 1, player.opponent);
			if (beta > util) {
				min_util = util;
				beta = min_util/18;
			}
		}
		player.undoMove(move);
		return min_util;
    }

    // FUNCTION OF CALCULATING EXPECIMINMAX VALUE
    // @PARAM: PLAYER INDEX(1 REPRESENTS MIN, 0 REPRESENTS MAX), CURRENT VALUE OF ALPHA, CURRENT VALUE OF BETA, CURRENT DEPTH
    // @RETURN  A DOUBLE VALUE REPRESENTING THE EXPECIMINMAX VALUE
    private double expectiMaxMin_alpha_beta(double alpha, double beta, int depth, ABbot player){
    	double expectiValue = 0;
    	ArrayList<Move> moves = player.generateMoves2();
    	if(moves.size()!=0) {
	    	// if it is min's turn
	    	if(player.id == 1){
	    		double min = Integer.MAX_VALUE;
				for (Move move : moves) {
					min = Math.min(min, minMove(move, alpha, beta, depth, player));
				}
	    	  	expectiValue = min;
	    	}
	    	// if it is max's turn
	    	else if(player.id == 0){
	    		double max = Integer.MIN_VALUE;
				for (Move move : moves) {
					max = Math.max(max, maxMove(move, alpha, beta, depth, player));
				}
	    		expectiValue = max;
	    	}
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
		return possible_moves;
    }
    
    public void makeMove(Move move) {
        this.B.botMove(move);

    }

    public void makeMove(int from, int to) {
        this.B.BotMove(from, to);

    }
    
    public void makeMove(int from, int to, Move move) {
        this.B.botMove(move);

    }

    public void undoMove(Move move) {
        boolean didKill = move.isKill;
        boolean didOut = move.isMoveOut;
        makeMove(move);
        if (didKill) {
            if (id == 0) {
                makeMove(0, move.to, move);
            } else {
                makeMove(25, move.to,move);
            }
        }
        if (didOut) {
            makeMove(26, move.to,move);

        }
    }

    
 // evaluation function to evaluate the whole board status
    public double EvaluationFunction(){
        return this.OtherPiecesSlain()*evaluator[0] + this.pipCount()*evaluator[1] + this.DoneScore()*evaluator[2] + 
        		this.DoneBoardScore()*evaluator[3] + this.piecesAlone()*evaluator[4];
    }
    public double OtherPiecesSlain(){
        for(Space space : this.B.getSpaces()){
               if(space.getSize()==2 && (space.getPieces().get(0).getId() + space.getPieces().get(1).getId()) == 1 ){
                   return 1;
               }
        }
        return 0;
    }
    public double pipCount(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double pip = 0;
            for (int i = 1; i <= 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += Math.pow(i,2);
                    }
                }
            }
            if(this.B.getSpaces()[0].getSize() > 0 && this.B.getSpaces()[0].getPieces().get(0).getId() == 1){
                pip += this.B.getSpaces()[0].getSize()*24;
            }
            return - pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        pip += Math.pow(25 - i,2);
                    }
                }
            }
            return - pip;
        }
    }
    public double DoneScore(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
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
    public double DoneBoardScore(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double score = 0;
            for (int i = 1; i < 7; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    score =+ this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        } else {
            double score = 0;
            for (int i = 19; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    score =+ this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        }
    }
    public double piecesAlone(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
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
