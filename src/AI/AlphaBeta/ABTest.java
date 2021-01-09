package AI.AlphaBeta;

import World.Board;

import java.util.ArrayList;

public class ABTest {
    public static void main(String[] args) {
        Board board = new Board();
        AlphaBetaBot alphaBetaBot = new AlphaBetaBot(0);
        alphaBetaBot.opponent.setBoard(board);
        board.setPlayers(alphaBetaBot,alphaBetaBot.opponent);
        board.createBotLoop();
        Move a = alphaBetaBot.getBestMove();
        System.out.println(a);

//        ArrayList<Move> moves = one.generateMoves2();
//        Move m11 = moves.get(0);
//        one.makeMove(m11);
//        System.out.println(board);
//        moves  = one.generateMoves2();
//        Move m12 = moves.get(0);
//        one.makeMove(m12);
//        System.out.println(board);
//        board.getGameLoop().process();
//
//        ArrayList<Move> moves2 = two.generateMoves2();
//        Move m21 = moves2.get(0);
//        two.makeMove(m21);
//        System.out.println(board);
//        moves2 = two.generateMoves2();
//        Move m22 = moves2.get(0);
//        two.makeMove(m22);
//        System.out.println(board);
//
//        two.undoMove(m22);
//        two.undoMove(m21);
//        one.undoMove(m12);
//        one.undoMove(m11);
//        System.out.println(board);
    }
}
