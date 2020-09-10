package src.khai;

import java.util.ArrayList;

public class Point {
    ArrayList<Piece> pieces;
    int id;
    int playerId;
    boolean isHome;
    int dominantId;

    public Point(int id, int playerId) {
        this.id = id;
        this.playerId = playerId;
        if ((this.playerId == 1 && this.id < 6) || (this.id > 17 && this.playerId == 2)) {
            isHome = true;
        }
        pieces = new ArrayList<>();

    }

    public void addPiece(Piece piece) {

        this.pieces.add(piece);
    }

    public int getSize() {
        return this.pieces.size();
    }

    public void setDominantId() {
        if (pieces.size() > 0) {
            dominantId = pieces.get(0).id;
        }
    }

    @Override
    public String toString() {
        return "Point: " + id +
                " Player: " + playerId +
                " is Home: " + isHome
                + " currently dominated by: " + dominantId;
    }
}
