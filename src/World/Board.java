package World;

import java.util.Arrays;

public class Board {
    private Space[] spaces;
    private Die die;

    public Board(){
        Die die=new Die();
        die.generateDie();

        spaces = new Space[25];//0 is for the "eaten" ,1-12 is the bottom(right-left), 13-24 is top (Left to right)
        createSpaces();

        addPieces(1,2,0);
        addPieces(6,5,1);
        addPieces(8,3,1);
        addPieces(12,5,0);

        addPieces(13,5,1);
        addPieces(17,3,0);
        addPieces(19,5,0);
        addPieces(24,2,1);

    }
    //methods for board creation
    private void addPieces(int spaceIndex, int num, int colorId) {
        for(int i = 0;i< num;i++)
            spaces[spaceIndex].getPieces().add(new Piece(colorId));
    }
    private void createSpaces() {
        for (int i = 0; i < spaces.length; i++) {
            spaces[i] = new Space(i);
        }
    }


    public String toString() {
        String res=String.format("%2d  "+spaces[0]+ "\n", 0);
        for(int i=1;i<=12;i++){
            res+= String.format("%2d  %15s | %15s  %2d\n", i, spaces[i], spaces[25-i],(25-i));
        }

        return res;

    }

    public Space[] getSpaces(){
        return spaces;
    }

//    public Space[] getValidMoves(Space selected){
//        Space[] res = new Space[3];
//        int[] roll= die.getCurRoll();
//        //if the piece is red, the movement is from 24->1 so make the roll -ve
//        if(selected.getPieces().get(0).id==1)
//            for(int i=0;i< roll.length;i++) roll[i]*=-1;
//
//
//        for(int i=0;i< roll.length;i++) {
//            //check if move is inbound
//            if (selected.getLabel() + roll[0] < 25 && selected.getLabel() + roll[0] >=0 ) {
//                if()
//            }
//        }
//        if()
//        return res;
//    }
    public boolean playerMove(Space from, Space to){
        return true;
    }

}









