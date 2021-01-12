package AI.AlphaBeta;

import World.Board;

import java.util.ArrayList;
public class ABTest {
    public static void main(String[] args) {
        AlphaBetaBot player0 = new AlphaBetaBot(0);
      ;

        Turn turn = player0.getBestMove();
        System.out.println(turn);
        System.out.println(player0.B);
    }
}