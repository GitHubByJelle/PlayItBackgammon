package Utils;

import java.awt.*;

public final class Variables {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double FRAME_WIDTH= 900 ;
    public static final double FRAME_HEIGHT = 650;


    //colors
    public static Color RED_PIECE_COLOR = Color.BLACK;
    public static Color WHITE_PIECE_COLOR = Color.WHITE;

    public static Color ODD_SPACES_COLOR = new Color(156, 89, 60);
    public static Color EVEN_SPACES_COLOR = new Color(91, 61, 41);
    public static Color OUT_OF_PLAY_SPACE_COLOR= new Color(156, 89, 60);
    public static Color RECOLOR_SPACES_COLOR =new Color(250,20,20,50);

    public static Color GAME_BACKGROUND_COLOR = new Color(213, 165, 117);

    //fornow its gonna be like this, later we can change it to store AI objects @ali
    public static String[] PLAYERS= {"Human", "BotA", "BotB"};

}
