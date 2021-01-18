package AI.AlphaBeta;

import World.Board;

import java.util.ArrayList;
public class ABTest {
    public static void main(String[] args) {
        AlphaBetaBot player0 = new AlphaBetaBot(1);
        System.out.println(player0.B);
        Turn t = player0.getBestMove();
        System.out.println(t);
    }
   // public static void abc(){
//        AlphaBetaBot player1 = new AlphaBetaBot(1);
//        player0.setOpponent(player1);
//        Board b = new Board();
//        b.setPlayers(player0,player1);
//        player0.setBoard(b);
//        player1.setBoard(b);
//
//        System.out.println(b);
//        Move move = new Move(1,3,0);
//        Move move2 = new Move(1,2,0);
//        Turn t = new Turn();
//        t.addMoves(move);
//        t.addMoves(move2);
//        System.out.println(t);
//        player0.makeTurn(t,1);
//        System.out.println(b);
//        Move move11 = new Move(6,3,1);
//        Move move12 = new Move(6,2,1);
//        Turn turn = new Turn();
//        turn.addMoves(move11);
//        turn.addMoves(move12);
//        player1.makeTurn(turn,1);
//        System.out.println(b);
//
//        player1.unDoTurn(turn);
//        System.out.println(b);
//    }
}