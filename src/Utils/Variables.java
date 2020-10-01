package Utils;

import java.awt.*;

public final class Variables {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double SCREEN_WIDTH=  screenSize.getWidth();
    public static double  SCREEN_HEIGHT= screenSize.getHeight();
    public static double FRAME_WIDTH= 900 ;
    public static double FRAME_HEIGHT = 650;


    //colors
    public static Color RED_PIECE_COLOR = Color.RED;//change these later
    public static Color WHITE_PIECE_COLOR = Color.WHITE;

    public static Color ODD_SPACES_COLOR =new Color(204,153,90);
    public static Color EVEN_SPACES_COLOR = Color.BLACK;
    public static Color RECOLOR_SPACES_COLOR =new Color(250,20,20,50);

    public static Color GAME_BACKGROUND_COLOR = new Color(80,46,41);

    //fornow its gonna be like this, later we can change it to store AI objects @ali
    public static String[] PLAYERS= {"Human", "BotA", "BotB"};

}
