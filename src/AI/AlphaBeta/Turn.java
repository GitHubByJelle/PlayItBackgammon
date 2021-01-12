package AI.AlphaBeta;

import AI.AlphaBeta.Move;

import java.util.ArrayList;

public class Turn {
    public ArrayList<Move> moves = new ArrayList<Move>();
    int index =  0;
    public Turn(){
        moves = new ArrayList<>();
    }
    public void addMoves(Move move){
        moves.add(move);
        assert moves.size() <= 4;
    }
    public ArrayList<Move> getMoves(){
        return this.moves;
    }
    @Override
    public String toString() {
        return "Turn{" +
                moves +
                '}';
    }
}