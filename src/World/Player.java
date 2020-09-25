package World;

import Utils.Variables;

public class Player {
    private int id;
    private int piecesInPlay;
    private int piecesOutOfPlay;
    private int piecesSlain;//or eaten
    private String type;

    //player id should match the piece id for our sakes
    public Player(int id){
        this.id=id;
        piecesInPlay=15;
        piecesOutOfPlay=0;
        piecesSlain=0;
        type= Variables.PLAYERS[0];
    }

    public String toString(){
        return "Player "+ type+" " +id;
    }



}
