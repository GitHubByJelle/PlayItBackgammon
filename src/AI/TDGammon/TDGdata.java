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


    double [] data= new double[4*27*2];//features
    public TDGdata(Board b){
        Space[]space = new Space[26];
        for(int i=0; i<space.length;i++){
            space[i]=b.getSpaces()[i];
        }
        Space out=b.getOutOfPlay();
        int s=0;
        int pieceCount=0;


        System.out.println("Space Length: " + space.length);

        //0, 25 are eaten spaces
        //26 is out space
        //1-24 are play spaces
        for(int id=0;id<2;id++) {
            int i=0;
            for ( i = 0; i < space.length; i++) {
                s = 27 * 4 * id + 4 * i;
                //if w/r pieces
                if (!space[i].isEmpty()) {
                    //the space is not empty
                    //0 id is for white
                    if (space[i].getPieces().get(0).getId() == id) {
                        s = 27 * 4 * id + 4 * i;
                        System.out.println(space[i].getPieces().size());
                        if (space[i].getPieces().size() >= 1)
                            data[s] = 1;
                        if (space[i].getPieces().size() >= 2)
                            data[s + 1] = 1;
                        if (space[i].getPieces().size() >= 3)
                            data[s + 2] = 1;
                        if (space[i].getPieces().size() > 3) {
                            data[s + 3] = (space[i].getPieces().size() - 3) / 2.0;
                            System.out.println((space[i].getPieces().size() - 3) / 2.0);
                        }
                    }
                }
                System.out.println(space[i].getId()+" "+ s+" "+ data[s] + " " + data[s + 1] + " " + data[s + 2] + " " + data[s + 3]);
                System.out.println("____________________");
            }
            System.out.println(i);
            s = 27 * 4 * id + 4 * (i);
            pieceCount = countID(out, id);
            if (pieceCount >= 1)
                data[s] = 1;
            if (pieceCount >= 2)
                data[s+1] = 1;
            if (pieceCount >= 3)
                data[s+2] = 1;
            if (pieceCount >3)
                data[s+3] = (pieceCount - 3) / 2.0;

            System.out.println(i+" "+ s+" "+ data[s] + " " + data[s + 1] + " " + data[s + 2] + " " + data[s + 3]);
            System.out.println("Last number of id " + id + " is " + (s+3) + "\n");
            System.out.println("Should be " + (4*27+id*4*27 - 1) + "\n");
        }
    }


    private int countID(Space s, int id){
        int res =0;
        if(s.isEmpty()) return 0;
        else{
            for(int i=0;i<s.getPieces().size();i++){
                if(s.getPieces().get(i).getId()==id) ++res;
            }
        }
        return res;
    }
    public void print(){
        //stream(data).forEach(System.out::println);

        for(int i=0;i< data.length;i++){
            System.out.println(i+" " +data[i]);
            if(i==108){
                System.out.println("________________");
            }
        }
    }
}
