package World;

import java.util.ArrayList;

public class Space {
    private int x,y;
    private ArrayList<Piece> pieces;


    private int playerId; // The playerId, would be either 1 or 2
    private boolean isHome; //Is this space in the player's home-zone?
    private int dominantId; //The space belongs to player 1 or 2 ?
    private int id; // The space's id in {0...23}


    public boolean movePiece(Space to){
        Piece p= pieces.get(0);
        pieces.remove(p);
        to.getPieces().add(p);

        return true;
    }

    public ArrayList<Piece> getPieces(){return pieces;}

    public void addPiece(Piece piece){
        this.pieces.add(piece);
    }

    public Space(int id, int playerId) {
        this.id = id;
        this.playerId = playerId;
        if ((this.playerId == 1 && this.id < 6) || (this.id > 17 && this.playerId == 2)) {
            isHome = true;
        }
        pieces = new ArrayList<>();

    }



    public int getSize() {
        return this.pieces.size();
    }

    public void setDominantId() {
        if (pieces.size() > 0) {
            dominantId = pieces.get(0).getId();
        }
    }

    @Override
    public String toString() {
        return "Space: " + id +
                " Player: " + playerId +
                " is Home: " + isHome
                + " currently dominated by: " + dominantId;
    }
    public int getId(){
        return this.id;
    }
    public int getDominantId(){
        return this.dominantId;
    }


}

