package AI;

public class Turn {
    Move[] moves;
    public Move move1;
    public Move move2;
    int index =  0;
    public Turn(){
        moves = new Move[2];
    }
    public void setMoves(Move move){
        if(index == 0){
            moves[0] = move;
            move1 = move;
            index++;
        }else if (index == 1){
            moves[1] = move;
            move2 = move;
        }
    }

    @Override
    public String toString() {
        return "Turn{" +
                ", move1=" + move1 +
                ", move2=" + move2 +
                ", index=" + index +
                '}';
    }
}