package World;

import Utils.Variables;

import java.awt.*;

public class Piece {
    public int id;//0 for white, 1 for red
    public Color color;
    public boolean isHome=false;
    public Piece(int clr){
        if(clr==0)
            color= Variables.WHITE_PIECE_COLOR;
        else
            color=Variables.RED_PIECE_COLOR;
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

    public Color getColor(){
        return color;
    }

    public void setColor(Color c){
        color=c;
    }


}
