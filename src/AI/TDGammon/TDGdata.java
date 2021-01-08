package AI.TDGammon;

import World.Board;
import World.Space;

import java.util.Arrays;

import static java.util.Arrays.stream;

public class TDGdata {
    public static void main(String[] args){
        Board b=new Board();
        TDGdata d= new TDGdata(b);
        //d.print();
    }


    float [] data= new float[4*27*2];//4 features, 27 spaces, 2 players
    public TDGdata(Board b){
        //26 spaces in the board
        Space[]space = b.getSpaces();
        Space out=b.getOutOfPlay();// out of play space in the board(can contain both red and white pieces)
        int s=0;//(for the index in data)
        int pieceCount=0;//(for the out space since in can contain both colors)


        //System.out.println("Space Length: " + space.length);

        //0, 25 are eaten spaces
        //26 is out space
        //1-24 are play spaces
        for(int id=0;id<2;id++) {
            int i=0;
            for (i = 0; i < space.length; i++) {
                //COUNTING THE NUMBER PIECES IN-PLAY AND ADDING TO FEATURE VECTOR data
                //27 is the total number of spaces we are storing data for
                //4 is the number of features in each space per player
                    //first three pieces are their own feature(x1,x2,x3) and the fourth feature is for having more than 3 pieces
                    //{x1,x2,x3,(n-3)/2} where n is the number of pieces
                    //{1,1,1,1} would be a space with more than 3 pieces
                    //{1,1,0,0} would be a space with 2 pieces
                //second 4 and i are for the indices so that data doesnt overlap
                s = 27 * 4 * id + 4 * i;
                if (!space[i].isEmpty()) {
                    //the space is not empty
                    //0 id is for white, 1 is for red
                    if (space[i].getPieces().get(0).getId() == id) {
                        System.out.println(space[i].getPieces().size());
                        if (space[i].getPieces().size() >= 1)
                            data[s] = 1;
                        if (space[i].getPieces().size() >= 2)
                            data[s + 1] = 1;
                        if (space[i].getPieces().size() >= 3)
                            data[s + 2] = 1;
                        if (space[i].getPieces().size() > 3) {
                            data[s + 3] = (space[i].getPieces().size() - 3) / 2f;
                          //  System.out.println((space[i].getPieces().size() - 3) / 2.0);
                        }
                    }
                }
//                System.out.println(space[i].getId()+" "+ s+" "+ data[s] + " " + data[s + 1] + " " + data[s + 2] + " " + data[s + 3]);
//                System.out.println("____________________");
            }
//            System.out.println(i);
            s = 27 * 4 * id + 4 * (i);
            //COUNTING THE NUMBER OF PIECES OUT-OF-PLAY AND ADDING TO FEATURE VECTOR data
            //count the pieces of the current player(0 or 1) to use instead of the getPieces().size() because the out
            //space can contain both colors
            pieceCount = countID(out, id);
            if (pieceCount >= 1)
                data[s] = 1;
            if (pieceCount >= 2)
                data[s+1] = 1;
            if (pieceCount >= 3)
                data[s+2] = 1;
            if (pieceCount >3)
                data[s+3] = (pieceCount - 3) / 2f;

//            System.out.println(i+" "+ s+" "+ data[s] + " " + data[s + 1] + " " + data[s + 2] + " " + data[s + 3]);
//            System.out.println("Last number of id " + id + " is " + (s+3) + "\n");
//            System.out.println("Should be " + (4*27+id*4*27 - 1) + "\n");
        }
    }

    //given a space and a piece id, returns the int number of pieces in that space with a matching id
    private int countID(Space s, int id){
        int res =0;
        if(s.isEmpty()) return 0;//if the space is empty return 0
        else{
            //otherwise add the number of pieces matching the given id to the final return
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
