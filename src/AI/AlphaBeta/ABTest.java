package AI.AlphaBeta;

import World.Board;

import java.util.ArrayList;
public class ABTest {
    public static void main(String[] args) {
        AlphaBetaBot player0 = new AlphaBetaBot(1);
      ;
        ArrayList<Turn> turns = player0.getValidTurns(1);
        System.out.println(turns);
        System.out.println(player0.B);
    }
}