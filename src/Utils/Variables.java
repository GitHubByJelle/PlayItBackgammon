package Utils;

import AI.BotA;
import World.Player;

import java.awt.*;

public final class Variables {


    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double FRAME_WIDTH= 900 ;
    public static final double FRAME_HEIGHT = 650;


    //colors
    public static final Color RED_PIECE_COLOR = new Color(190,0,0);
    public static final Color WHITE_PIECE_COLOR = Color.WHITE;

    public static final Color ODD_SPACES_COLOR = new Color(156, 89, 60);
    public static final Color EVEN_SPACES_COLOR = new Color(91, 61, 41);
    public static final Color OUT_OF_PLAY_SPACE_COLOR= new Color(91, 61, 41);
    public static final Color RECOLOR_SPACES_COLOR =new Color(250,20,20,50);

    public static final Color GAME_BACKGROUND_COLOR = new Color(213, 165, 117);


    public static final String HUMAN = "Human";
    public static final String BOTA = "BotA";
    public static final String SIMPLEBOT="SimpleBot";
    public static final String BP="PrimeBlitzingBot";

    //fornow its gonna be like this, later we can change it to store AI objects @ali
    public static final String[] PLAYERS= {HUMAN, BOTA, SIMPLEBOT,BP};


}
