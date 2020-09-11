package World;

import java.util.ArrayList;

public class Space {

    private ArrayList<Piece> pieces;
    private int id;
    private int playerId;
    private boolean isHome;
    private int dominantId;


    public boolean movePiece(Space to) {
        Piece p = pieces.get(0);
        pieces.remove(p);
        to.getPieces().add(p);
        return true;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }


    public void addPiece(Piece piece){
        this.pieces.add(piece);
    }

    public Space(int id, int playerId) {
        this.id = id;
        this.playerId = playerId;
        if ((this.playerId == 1 && this.id < 6) || (this.id > 19 && this.playerId == 2)) {
            isHome = true;
        }
    }

    public String toString(){
        String res = "";
        for(int i=0;i<pieces.size();i++){
            res +=pieces.get(i);
        }
        return res;
    }

    public boolean isEmpty(){
        return pieces.size()==0;
    }

    public int getId(){
        return id;
    }


    public void setDominantId() {
        if (pieces.size() > 0) {
            dominantId = pieces.get(0).getId();
        }
    }

    public int getSize() {
        return this.pieces.size();
    }
    public int getDominantId(){
        return this.dominantId;
    }
}
