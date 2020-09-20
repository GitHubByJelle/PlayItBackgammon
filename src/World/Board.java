package World;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Board {
    private Space[] spaces;
    private Die die;
    private Space eatenSpace;
    public Board(){
        die=new Die();
        die.generateDie();
        die.getNextRoll();//remove this later

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
        eatenSpace = spaces[24];

    }
    //methods for board creation
    private void addPieces(int spaceIndex, int num, int colorId) {
        for(int i = 0;i< num;i++)
            spaces[spaceIndex].getPieces().add(new Piece(colorId));
    }
    private void createSpaces() {
        int x=0;
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

    public ArrayList<Space> getValidMoves(Space selected){

        ArrayList<Space> res = new ArrayList<Space>();
        Space target;
        int[] roll= die.getCurRoll();
        //check for double roll
        if(die.isDouble(roll)) roll = new int[]{roll[0],roll[0],roll[0],roll[0]};
        //if the piece is red, the movement is from 24->1 so make the roll -ve
        if(selected.getPieces().get(0).id==1)
            for(int i=0;i< roll.length;i++){
                roll[i]*=-1;
                System.out.println(roll[i]);
            }

        for(int i=0;i< roll.length;i++) {
            //check for inbounds
            if(selected.getId()+roll[i]<25 || selected.getId()+roll[i]>-1) {
                target=spaces[selected.getId() + roll[i]];

                if(validityCheck(selected, target))
                    res.add(spaces[selected.getId() + roll[i]]);
                System.out.println(spaces[selected.getId() + roll[i]].getId());
            }else{
                //check if all the pieces are home in case the rolls can take the current piece out of play
            }
        }
        //comnbination of rolls
        //will only exist if we have atleast 2 valid moves
        if(res.size()>1){

        }


        return res;
    }
    //check if the target space is empty or if it has pieces of the same color, or if it has 1 piece of the opposite color
    public boolean validityCheck(Space selected, Space target) {
        return target.getPieces().size()==0 || target.getPieces().get(0).getId()==selected.getPieces().get(0).getId()||
                (target.getPieces().size()==1 &&target.getPieces().get(0).getId()!=selected.getPieces().get(0).getId());
    }

    public boolean playerMove(Space from, Space to){
        if(validityCheck(from, to)) {
            from.movePiece(to);
            return true;
        }else{
            System.out.println("Move invalid");
            return false;
        }
    }
    public boolean playerMove(int from, int to){
        if(validityCheck(spaces[from],spaces[to])) {
            spaces[from].movePiece(spaces[to]);
            return true;
        }else{
            System.out.println("Move invalid");
            return false;
        }
    }


    public Space getEatenSpace() {
        return this.eatenSpace;
    }

    public boolean playerMovePossibilities(int from) {
        boolean ans=false;
        for (int i = 1; i < spaces.length; i++) {
            if (validityCheck(spaces[from], spaces[i])) {
                System.out.println("space " + i + " should be colored");
                ans=true;
            }
            else{
                System.out.println(i+ "is not a valid space");
            }
        }
        return ans;

    }
}










