package World;

import AI.AlphaBeta.Move;

import AI.AlphaBeta.Turn;
import Utils.Variables;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {

    public int id;//matches piece id
    public int piecesInPlay;
    public int piecesOutOfPlay;
    public int piecesSlain;//or eaten
    public Bot opponent;

    Player(int id){
        this.id=id;
        piecesInPlay=15;
        piecesOutOfPlay=0;
        piecesSlain=0;
    }

    public int getPiecesInPlay(){
        return piecesInPlay;
    }
    public void resetPlayer(){
        piecesInPlay=15;
        piecesOutOfPlay=0;
        piecesSlain=0;
    }

    public int getPiecesOutOfPlay(){
        return piecesOutOfPlay;
    }

    public int getPiecesSlain(){
        return piecesSlain;
    }

    public int getId() {
        return id;
    }
    public void pieceSlain(){
        --piecesInPlay;
        ++piecesSlain;
    }

    public void revivePiece(){
        --piecesSlain;
        ++piecesInPlay;
    }

    public void pieceOut(){
        --piecesInPlay;
        ++piecesOutOfPlay;
    }

    public abstract void executeTurn() ;
    public abstract void setBoard(Board B);
    public  abstract String getName();

    public  String toString(){ return "Player "+ id+ " "+ getName();};
    public void printSelectedMove(int from, int to){
        System.out.println("SELECTED MOVE FROM: "+ from+" TO "+to);
    }
    //Alaa is not responsible for this Method
    public void setOpponent(Bot opponent){
        this.opponent = opponent;
    }
//______________________________________________

    public static class Human extends Player{

        @Override
        public void executeTurn() {}

        @Override
        public void setBoard(Board B) {}

        @Override
        public String getName() {
            return "Human";
        }


        public Human(int id){
            super(id);
        }
    }

    public static class Bot extends Player{
        //things common to all bots
        public Board B;
        public boolean pausing = true;
         int[][] DIES_COMBINATION = new int[21][2];

        public Bot(int id) {
            super(id);
            generateAllDieCombination();
        }

        @Override
        public void executeTurn()  {
           // bot.executeTurn();
            B.getGameLoop().repaintBV();
            pauseBot();
        }

        public void pauseBot(){
            if(pausing){
                try{
                    Thread.sleep((100));
                }catch(InterruptedException e){
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                }
            }

        }

        public void requestPassTurn(){
            B.getGameLoop().changeTurn();
            //System.out.println("PASS TURN");
        }

        @Override
        public void setBoard(Board B) {
            this.B=B;
        }

        @Override
        public String getName() {
            return "";
        }




        //bot "looking" at the board
        protected ArrayList<Space[]> getPossibleMoves(ArrayList<Space> possFrom) {
            ArrayList<Space[]> res= new ArrayList<>();
            ArrayList<Space> possPer;//possible moves per possible selected from
            for(int i=0;i<possFrom.size();i++){
                possPer= B.getValidMoves(possFrom.get(i));
                if(possPer.size()>0){
                    for(int l=0;l<possPer.size();l++){
                        res.add(new Space[]{ possFrom.get(i),possPer.get(l)});
                    }
                }
            }
            return res;
        }
        public int[] getRandomDie(){
            Random random = new Random();
            int r = random.nextInt(20);
            return DIES_COMBINATION[r];
        }
        public void generateAllDieCombination() {
            int index = 0;
            for (int i = 1; i < 7; i++) {
                for (int j = i; j < 7; j++) {
                    int[] die = {i, j};
                    DIES_COMBINATION[index++] = die;
                }
            }
        }
        protected ArrayList<Space> getPossibleFrom() {
            ArrayList<Space> selection = new ArrayList<>();
//            System.out.println("Epic");
//            System.out.println(B.toString());
            Space[] spaces= B.getSpaces();
            if(!spaces[B.getGameLoop().getSlainSpace(id)].isEmpty()){
                selection.add(spaces[B.getGameLoop().getSlainSpace(id)]);
            }else{
                if(id==0){
                    for(int i=1;i<= spaces.length-2; i++){
                        if(!spaces[i].getPieces().isEmpty())
                            if(spaces[i].getPieces().get(0).getId()==id){
                                selection.add(spaces[i]);
                            }
                    }
                }else{
                    for(int i=spaces.length-2; i>=1 ;i--){
                        if(!spaces[i].getPieces().isEmpty())
                            if(spaces[i].getPieces().get(0).getId()==id){
                                selection.add(spaces[i]);
                            }
                    }
                }
            }
            return selection;

        }

        protected ArrayList<Space[]> getProbableMoves(ArrayList<Space> possFrom, int id) {
            Set<DummyMove> res= new HashSet<>();
            int[] curRoll;
            DummyMove newEntry;
            ArrayList<Space> probMove;//probable move per possible selected from[from, to]
            ArrayList<int[]> possRolls= Variables.All_POSS_ROLLS;//all possible rolls with 2 dies, doubles are accounted for
            for(int i=0;i<possFrom.size();i++){
               for(int rollIndex=0;rollIndex<possRolls.size();rollIndex++) {
                   curRoll = signCheck(possRolls.get(rollIndex), id);
                   probMove = B.getDeepValidMoves(possFrom.get(i), curRoll);
                   for (int a = 0; a < probMove.size(); a++) {
                       newEntry = new DummyMove(possFrom.get(i), probMove.get(a));
                       newEntry.setRoll(curRoll);
                       res.add(newEntry);
                   }

               }


            }

            ArrayList<DummyMove> a = new ArrayList<>(res);
//            for( int i=0;i<a.size();i++){
//                System.out.println(a.get(i));
//            }
            return DummyMove.toArrayListofSpace(a);
        }

        private int[] signCheck(int[] ints, int id) {
            int[] res= new int[ints.length];
            int sign;
            if(id==0) sign=1;
            else sign = -1;
            for(int i=0;i<ints.length;i++){
                if(sign ==-1 && ints[i]>0){
                    res[i]=ints[i]*-1;
                }else if(sign==1 && ints[i]<0){
                    res[i]= ints[i]*-1;
                }else{
                    res[i] = ints[i];
                }
            }


            return res;
        }

        public ArrayList<Space[]> preformOp(){
            ArrayList<Space> a= getPossibleFrom();
            return getProbableMoves(a,id);

        }




        //Methods Alaa is not responsible for__________________________________________
        public ArrayList<Turn> getValidTurns(){
            return this.B.getValidTurns(this.B.getDie().getCurRoll(),this.id);
        }
        public ArrayList<Turn> getValidTurns(int id){
            if(id == 0){
                return this.B.getValidTurns(this.B.getDie().getCurRoll(),this.id);
            }else{

                int[] die = Arrays.copyOf(this.B.getDie().getCurRoll(),this.B.getDie().getCurRoll().length);
                for(int  i = 0; i  < die.length; i++){
                    die[i] = -die[i];
                }
                this.B.getDie().setCurRoll(die);
                return this.B.getValidTurns(this.B.getDie().getCurRoll(),this.id);

            }
        }

        public void makeTurn(Turn turn,int dummy){
            int[] temp = Arrays.copyOf(this.B.getDie().getCurRoll(),this.B.getDie().getCurRoll().length);
            ArrayList<Move> moves = turn.moves;

            for(Move move: moves){
                this.B.botMove(move);
            }
            this.B.getDie().setCurRoll(temp);
        }
        public void unDoTurn(Turn turn) {
            for(int i = turn.moves.size()-1; i>-1; i--){
                this.B.undoBotMove(turn.moves.get(i));
            }
        }
        public void unDoTurn(Turn turn,int dummy) {
            for(int i = turn.moves.size()-1; i>-1; i--){
                this.B.undoBotMove(turn.moves.get(i));
            }
        }
        public ArrayList<Move> generateMoves2() {
            ArrayList<Move> possible_moves = new ArrayList<>();
            Space[] allSpaces = this.B.getSpaces();
            for (Space space : allSpaces) {
                if (space.getSize() > 0) {
                    if (space.getPieces().get(0).getId() == this.id) {
                        ArrayList<Space> validMoves = this.B.getValidMoves(space);
                        if (validMoves.size() > 0) {
                            for (Space v : validMoves) {
                                Move move = new Move(space.getId(), v.getId(), this.id);
                                possible_moves.add(move);
                            }
                        }
                    }
                }
            }
            //System.out.println(possible_moves);
            return possible_moves;
        }

        public void makeMove(Move move) {
            this.B.BotMove(move);
        }

        public void undoMove(Move move) {
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
            int temp = move.to;
            move.to = move.from;
            move.from = temp;
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
            this.B.undoBotMove(move);

        }

        public int[] getDie(int i){
            return DIES_COMBINATION[i];
        }


    }
    //_______________________________________________________________________________


}

//this is Alaa's class.
class DummyMove{
    Space[] move;
    int [] roll;
    public DummyMove(Space from, Space to){
        this.move = new Space[]{from, to};
    }

    public void setRoll(int [] r){this.roll=r;}
    private int[] getRoll(){
        return roll;
    }

    Space []getMove(){
        return move;
    }

    @Override
    public boolean equals(Object other){
        if(this!=null && other!=null && ((DummyMove)other).getMove()[0].getId() ==move[0].getId()
                && ((DummyMove)other).getMove()[1].getId() == move[1].getId() && roll[0]==((DummyMove)other).getRoll()[0]
        && roll[1]==((DummyMove)other).getRoll()[1]) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return move[0].getId()-move[1].getId();
    }

    public static ArrayList<Space[]> toArrayListofSpace(ArrayList<DummyMove> list){
        ArrayList<Space[]> res = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            res.add(list.get(i).move);
        }
        return res;
    }

    @Override
    public String toString(){
        return "["+move[0].getId()+" ,"+move[1].getId()+"] "+ Arrays.toString(roll);
    }

}