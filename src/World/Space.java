package World;

import java.util.ArrayList;

public class Space {
    private int x,y;
    private ArrayList<Piece> pieces;

    public Space(int x,int y){
        this.x=x;
        this.y=y;
        pieces = new ArrayList<Piece>();
    }

    public boolean movePiece(Space to){

        return true;
    }

    private ArrayList<Piece> getPieces(){return pieces;}
}
