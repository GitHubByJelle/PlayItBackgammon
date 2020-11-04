package World;

import java.util.ArrayList;

public abstract class Player {

    public int id;//matches piece id
    public int piecesInPlay;
    public int piecesOutOfPlay;
    public int piecesSlain;//or eaten


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
        public boolean pausing =true;
        public Bot(int id){
            super(id);
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
                    Thread.sleep((2000));
                }catch(InterruptedException e){
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                }
            }

        }

        public void requestPassTurn(){
            B.getGameLoop().changeTurn();
            System.out.println("PASS TURN");
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

        protected ArrayList<Space> getPossibleFrom(){
            ArrayList<Space> selection = new ArrayList<>();
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



    }
}
