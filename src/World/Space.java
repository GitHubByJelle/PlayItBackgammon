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
        Piece p= pieces.get(0);
        pieces.remove(p);
        to.getPieces().add(p);

        return true;
    }

    public ArrayList<Piece> getPieces(){return pieces;}

    public String toString(){
        String res = "";
        if(y==2){
            res="_";
        }
        for(int i=0;i<pieces.size();i++){
            res +=pieces.get(i);
        }
        if(y==1){
            res+="_";
        }
        return res;
    }
}
