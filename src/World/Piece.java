package World;

public class Piece {
    private int id;//0 for white, 1 for red

    public Piece(int clr){
        this.id=clr;
    }


    public String toString(){
        String res;
        if(id==0){
            res="W";
        }else{
            res="R";
        }

        return res;
    }



}
