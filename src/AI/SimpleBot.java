package AI;

import World.Player;
import World.Space;

import java.util.ArrayList;

public class SimpleBot extends Player.Bot{
    //mainly to see that everything in the testing class works as we expect
    //also easier to see what you need for other bots
    //dumbest, will execute the first move it can looking from beginning->home direction
    public SimpleBot(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "SimpleBot";
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        turnChoice();
        pauseBot();
        B.getGameLoop().repaintBV();

    }

    private void turnChoice(){
        ArrayList<Space> possFrom = getPossibleFrom();
        ArrayList<Space[]> possMoves= getPossibleMoves(possFrom);
        int index=0;
        if(possMoves.size()==0)
            requestPassTurn();
        else
            while(!B.playerMove(possMoves.get(index)[0].getId(),possMoves.get(index)[1].getId())){
                if(index>possMoves.size()-1){
                    requestPassTurn();
                }
                ++index;
            }
        printSelectedMove(possMoves.get(index)[0].getId(),possMoves.get(index)[1].getId());
    }


}
