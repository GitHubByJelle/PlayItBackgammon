package AI;

import World.Player;
import World.Space;
import java.util.ArrayList;

public class PrimeBlitzingBot extends Player.Bot{

    public PrimeBlitzingBot(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "PrimeBlitzingBot";
    }

    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
       //choice of blitzing or priming
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    public void priming(){

    }

    public void blitzing(){

    }

    
}
