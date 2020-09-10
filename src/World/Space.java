package World;

import java.util.ArrayList;

public class Space {

    private ArrayList<Piece> pieces;

    public Space(){
        pieces = new ArrayList<Piece>();
    }



    public boolean movePiece(Space to){
        Piece p= pieces.get(0);
        pieces.remove(p);
        to.getPieces().add(p);

        return true;
    }

    public ArrayList<Piece> getPieces(){return pieces;}

    public String toString(){
        String res = "";
        for(int i=0;i<pieces.size();i++){
            res +=pieces.get(i);
        }
        return res;
    }
}
