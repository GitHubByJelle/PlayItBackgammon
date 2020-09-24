package World;


import java.util.ArrayList;

public class Board {
    private Space[] spaces;
    private Die die;
    private Space outOfPlay;
    public Board(){
        die=new Die();
        die.generateDie();
        die.getNextRoll();//remove this later

        spaces = new Space[25];//0 is for the "eaten" ,1-12 is the bottom(right-left), 13-24 is top (Left to right), out of play has id 26
        createSpaces();

        addPieces(1,2,0);
        addPieces(6,5,1);
        addPieces(8,3,1);
        addPieces(12,5,0);

        addPieces(13,5,1);
        addPieces(17,3,0);
        addPieces(19,5,0);
        addPieces(24,2,1);
        outOfPlay = new Space(26);

        //to correct for hishome values of the pieces
        for(int i=0;i< spaces.length;i++){
            for(int a=0;a<spaces[i].getPieces().size();a++){
                spaces[i].checkHome(spaces[i].getPieces().get(a));
            }
        }

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
        String res=String.format("%2d  %15s | %15s  %2d\n", 0, spaces[0], outOfPlay,(26));
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
        if(die.isDouble(roll))
            roll = new int[]{roll[0],roll[0],roll[0],roll[0]};
        //if the piece is red, the movement is from 24->1 so make the roll -ve
        if(selected.getPieces().get(0).id==1)
            for(int i=0;i< roll.length;i++){
                roll[i]*=-1;
                System.out.println(roll[i]);
            }

        for(int i=0;i< roll.length;i++) {
            //check for inbounds
            if(selected.getId()+roll[i]<25 && selected.getId()+roll[i]>0) {
                target=spaces[selected.getId() + roll[i]];

                if(validityCheck(selected, target))
                    res.add(spaces[selected.getId() + roll[i]]);
                System.out.println(spaces[selected.getId() + roll[i]].getId());
            }else{
                //check if all the pieces are home in case the rolls can take the current piece out of play(eaten Space)
                if(allPiecesHome(selected.getPieces().get(0).getId())){
                    res.add(outOfPlay);

                }
            }
        }

        return res;
    }


    private boolean allPiecesHome(int pieceID) {

        Piece cur;
        for(int i=0;i<spaces.length;i++){
            for(int x=0;x<spaces[i].getPieces().size();x++){
                cur =spaces[i].getPieces().get(x);
                if(cur.getId()==pieceID && !cur.isHome){
                    return false;
                }
            }
        }
        return true;
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
        return spaces[0];
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
    public Die getDice(){
        return die;
    }
    //Gets all valid moves
    public ArrayList<Space> getAllValidMoves(Board B){
        ArrayList returnSpaces = new ArrayList<Space>();
        for(Space space : B.getSpaces()){
            if(space.getSize() != 0) {
                returnSpaces.add(B.getValidMoves(space));
            }
        }
        return returnSpaces;
    }
}










