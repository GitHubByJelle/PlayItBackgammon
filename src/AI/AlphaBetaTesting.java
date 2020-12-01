package AI;

import World.Board;

public class AlphaBetaTesting {
	public static void main(String[] args) {
        Board b = new Board();
        AlphaBetaBot alphaBetaBot = new AlphaBetaBot(b,0);
        AlphaBetaBot opponent = new AlphaBetaBot(b,1);
        alphaBetaBot.setOpponent(opponent);
        opponent.setOpponent(alphaBetaBot);
        alphaBetaBot.alpha_beta_pruning_result();
//        System.out.println("Calling from main:"  + move);
//
//        alphaBetaBot.makeMove(move);
//        System.out.println(alphaBetaBot.B);
//        alphaBetaBot.alpha_beta_pruning_result();
//        System.out.println("Calling from main:"  + move);
//

    }

}
