package AI;

import World.Board;

public class AlphaBetaTesting {
	public static void main(String[] args) {
        Board b = new Board();
        ABbot alphaBetaBot = new ABbot(b,0);
        ABbot opponent = new ABbot(b,1);
        alphaBetaBot.setOpponent(opponent);
        opponent.setOpponent(alphaBetaBot);
        b.setPlayers(alphaBetaBot, opponent);
        b.createBotLoop();
        long a = System.nanoTime();
        alphaBetaBot.alpha_beta_pruning_result();
        System.out.println((System.nanoTime()-a)/1000000000.);
//        System.out.println("Calling from main:"  + move);
//
//        alphaBetaBot.makeMove(move);
//        System.out.println(alphaBetaBot.B);
//        alphaBetaBot.alpha_beta_pruning_result();
//        System.out.println("Calling from main:"  + move);
//

    }

}
