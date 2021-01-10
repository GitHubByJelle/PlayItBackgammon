package World;


import java.util.ArrayList;

public class Space {

    private ArrayList<Piece> pieces;
    private int id;



    public Space(int id) {
        pieces=new ArrayList<Piece>();
        this.id = id;
    }

    public boolean movePiece(Space to) {
        if(pieces.size() > 0) {
            Piece p = pieces.get(0);
//        System.out.println("PIECE BEING MOVED:"+p.getId());
            pieces.remove(p);
            to.getPieces().add(p);
            checkHome(p, to.getId());
        }
        return true;
    }
    public boolean moveBotPiece(Space to) {
        Piece p = pieces.get(0);
//        System.out.println("PIECE BEING MOVED:"+p.getId());
        pieces.remove(p);
        to.getPieces().add(0,p);
        checkHome(p,to.getId());
        return true;
    }



    public ArrayList<Piece> getPieces() {
        return pieces;
    }


    public void addPiece(Piece piece){
        this.pieces.add(piece);
    }
    public void removePiece(Piece piece){this.pieces.remove(piece);}

    public String toString(){
        String res ="";
        for(int i=0;i<pieces.size();i++){
            res +=pieces.get(i);
        }
        return res;
    }

    @Override
    public boolean equals(Object other){
        if(this !=null && other !=null && other instanceof Space && ((Space)other).getId()==id &&((Space)other).getSize()==getSize()){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public int hashCode(){
        return id*pieces.hashCode();
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



    private void checkHome(Piece a, int index){
        if(a.id==0){
            //piece is white then home is
            if(index>18) {
                a.isHome = true;
            }else{
                a.isHome = false;
            }
        }else{
            //piece is red
            if(index<7){
                a.isHome=true;
            }else{
                a.isHome = false;
            }
        }
    }


    public void checkHome(Piece a){
        if(a.id==0){
            //piece is white then home is
            if(id>18) {
                a.isHome = true;
            }else{
                a.isHome = false;
            }
        }else{
            //piece is red
            if(id<7){
                a.isHome=true;
            }else{
                a.isHome = false;
            }
        }
    }

}
