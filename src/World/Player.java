package World;

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

    public abstract void executeTurn();
    public  abstract String getName();
    public  String toString(){ return "Player "+ id+ " "+ getName();};

    public static class Human extends Player{
        @Override
        public void executeTurn() {
        }

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
        private Player bot;
        public Bot(int id, Player k) {
            super(id);
            this.bot=k;
        }

        @Override
        public void executeTurn() {
            bot.executeTurn();
        }

        @Override
        public String getName() {
            return bot.getName();
        }


    }
}
