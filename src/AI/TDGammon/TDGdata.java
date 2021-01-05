package AI.TDGammon;

import World.Board;
import World.Space;

import java.util.Arrays;

import static java.util.Arrays.stream;

public class TDGdata {
    public static void main(String[] args){
        Board b=new Board();
        TDGdata d= new TDGdata(b);
        d.print();



    }


    double [] data= new double[196];//features
    public TDGdata(Board b){
        Space[]space = new Space[27];
        for(int i=0; i<space.length-1;i++){
            space[i]=b.getSpaces()[i];
        }
        space[space.length-1]=b.getOutOfPlay();
        int s=0;

        //0, 25 are eaten spaces
        //26 is out space
        //1-24 are play spaces
        for(int id=0;id<2;id++)
            for(int i=0; i<space.length;i++){
                //if w/r pieces
                if(!space[i].isEmpty()) {
                    //the space is not empty
                    //0 id is for white
                    if(space[i].getPieces().get(0).getId()==id){
                        s=24 * 4 * id + 4 * i;
                        if(space[i].getPieces().size()==1)
                            data[s] = 1;
                        if(space[i].getPieces().size()==2)
                            data[s + 1] = 1;
                        if(space[i].getPieces().size()==3)
                            data[s + 2] = 1;
                        if(space[i].getPieces().size()>3) {
                            data[s + 3] = (space[i].getPieces().size() - 3) / 2.0;
                            System.out.println((space[i].getPieces().size() - 3) / 2.0);
                        }
                        System.out.println(data[s]+" "+data[s+1]+" "+data[s+2]+ " "+data[s+3]);
                        System.out.println("____________________");
                    }
                }
            }
    }

    public void print(){
        stream(data).forEach(System.out::println);
    }
}
