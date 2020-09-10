package Utils;

import java.awt.*;

public class Variables {
    //@Ali: I made this constructor to have access to the players String
    public Variables(){}
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double SCREEN_WIDTH=  screenSize.getWidth();
    public static double  SCREEN_HEIGHT= screenSize.getHeight();

    //fornow its gonna be like this, later we can change it to store AI objects @ali
    public String[] PLAYERS= {"Human", "BotA", "BotB"};
    public String[] getPlayers(){
        return PLAYERS;

    }
}
