package World;

import java.util.Arrays;

public class Board {
    private Space[] spaces;
    public Board(){
        spaces = new Space[25];//0 is for the "eaten" ,1-12 is the bottom(right-left), 13-24 is top (Left to right)
        createSide( 1);
        createSide(2);
        spaces[0]=new Space();
        addPieces(1,2,0);
        addPieces(6,5,1);
        addPieces(8,3,1);
        addPieces(12,5,0);

        addPieces(13,5,1);
        addPieces(17,3,0);
        addPieces(19,5,0);
        addPieces(24,2,1);

    }




    private void addPieces(int spaceIndex, int num, int colorId) {
        for(int i = 0;i< num;i++)
            spaces[spaceIndex].getPieces().add(new Piece(colorId));
    }

    private void createSide(int placement) {
        for (int i = 0; i < spaces.length; i++) {
            spaces[i] = new Space();
        }
    }


    public String toString() {
//        String[][] resAr= new String [spaces.length][5];
//
//        for(int i=0;i<spaces.length;i++){
//            for(int n=0;n<spaces[i].getPieces().size();n++){
//                resAr[i][n] = spaces[i].getPieces().get(n).toString();
//            }
//        }
//
//        return Arrays.deepToString(resAr).replace("], ", "]\n").replace("[[", "[").replace("]]", "]");
//
        String res="";
        for(int i=0;i<spaces.length;i++){
            res+= spaces[i]+ "\n";
        }

        return res;

    }



}









