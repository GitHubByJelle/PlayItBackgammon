package World;

import java.util.ArrayList;

public class Space {

    private ArrayList<Piece> pieces;
    private int id;
    private boolean isHome;
    private int domainID;


    public Space(int id) {
        pieces=new ArrayList<Piece>();
        if(id!=0){
            if(id>12)
                domainID=1;
            else
                domainID=2;
        }
        this.id = id;
    }

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

    public int getSize() {
        return this.pieces.size();
    }


    public int getDomainID(){
        return domainID;
    }

}
