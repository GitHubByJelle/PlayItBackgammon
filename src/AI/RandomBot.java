package AI;

import World.Player;
import World.Space;

import java.util.ArrayList;
import java.util.Random;

public class RandomBot extends Player.Bot {
    public RandomBot(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "RandomBot";
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
        //[FROM,TO] for all moves in possFrom
        Random r = new Random();
        int index;
        if(possMoves.size()==0)
            requestPassTurn();
        else {
            index=r.nextInt(possMoves.size());
            while (!B.playerMove(possMoves.get(index)[0].getId(), possMoves.get(index)[1].getId())) {
                index=r.nextInt(possMoves.size()) ;

            }
        }
        // printSelectedMove(possMoves.get(index)[0].getId(), possMoves.get(index)[1].getId());
    }


}
