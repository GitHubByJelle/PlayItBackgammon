package World;


public class Player {
    private int id;//matches piece id
    private int piecesInPlay;
    private int piecesOutOfPlay;
    private int piecesSlain;//or eaten
    private String type;


    //player id should match the piece id for our sakes
    public Player(int id, String type){
        this.id=id;
        piecesInPlay=15;
        piecesOutOfPlay=0;
        piecesSlain=0;
        this.type= type;
    }

    public String toString(){
        return "Player "+ type+" " +id;
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



}