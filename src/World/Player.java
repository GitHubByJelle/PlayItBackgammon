package World;

public class Player {
    private int id;
    private int piecesInPlay;
    private int piecesOutOfPlay;
    private int piecesSlain;

    //player id should match the piece id for our sakes

    public Player(int id){
        this.id=id;
        piecesInPlay=15;
        piecesOutOfPlay=0;
        piecesSlain=0;
    }


}
