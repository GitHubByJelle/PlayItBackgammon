package World;


import java.util.ArrayList;

public class Space {

    private ArrayList<Piece> pieces;
    private int id;
    private int domainID;


    public Space(int id) {
        pieces=new ArrayList<Piece>();
        if(id!=0){
            if(id==1 || id==12 || id==17 || id==19)
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
        checkHome(p,to.getId());
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

    public int getDominantId() {
        return this.domainID;
    }

    private void checkHome(Piece a, int index){
        if(a.id==0){
            //piece is white then home is
            if(index>18) {
                a.isHome = true;
            }
        }else{
            //piece is red
            if(index<7){
                a.isHome=true;
            }
        }
    }


    public void checkHome(Piece a){
        if(a.id==0){
            //piece is white then home is
            if(id>18) {
                a.isHome = true;
            }
        }else{
            //piece is red
            if(id<7){
                a.isHome=true;
            }
        }
    }
}
