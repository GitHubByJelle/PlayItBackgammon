package AI.AlphaBeta;

import World.Board;

import java.util.ArrayList;
public class ABTest {
    public static void main(String[] args) {
        Board board = new Board();
        AlphaBetaBot player0 = new AlphaBetaBot(0);
        player0.setBoard(board);
        board.setPlayers(player0, player0.opponent);
        player0.opponent.setBoard(board);
        board.createBotLoop();

        Turn turn = player0.getBestMove();
        System.out.println(turn);
        System.out.println(player0.B);
    }
}