package World;

public class Piece {
    public int id;//0 for white, 1 for red

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

    public int getId() {
        return id;
    }
}
