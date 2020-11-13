

public class Move {
    // Class represents a move
    public int from;
    public int to;
    public int playerId;
    public double score;
    public boolean isKill;
    public boolean isMoveOut;

    public Move(int from, int to, int playerId) {
        this.from = from;
        this.to = to;
        this.playerId = playerId;
    }

    public Move(int from, int to, double score) {
        this.from = from;
        this.to = to;
        this.score = score;
    }

    public void setKill(boolean isKill) {
        this.isKill = isKill;
    }

    public void setIsMoveOut(boolean isMoveOut) {
        this.isMoveOut = isMoveOut;
    }
}