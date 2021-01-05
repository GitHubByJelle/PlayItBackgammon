package AI.TDGammon;

import World.Player;

public class TDG extends Player.Bot{
    public TDG(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "TD-Gammon";
    }


    @Override
    public void executeTurn() {
        B.getGameLoop().repaintBV();
        moveChoice();
        pauseBot();
        B.getGameLoop().repaintBV();
    }

    private void moveChoice() {


    }
}
